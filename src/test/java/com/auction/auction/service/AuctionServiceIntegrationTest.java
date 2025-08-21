package com.auction.auction.service;

import com.auction.auction.model.AuctionStatus;
import com.auction.auction.model.Bid;
import com.auction.auction.model.Item;
import com.auction.auction.model.User;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
import com.auction.auction.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuctionServiceIntegrationTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BidRepository bidRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuctionService auctionService;
    @Autowired
    EntityManager entityManager;

    private User seller;
    private User bidder;

    @BeforeEach
    void setup() {
        seller = userRepository.save(User.builder().username("Jane Seller").email("jane@example.com").password("pass").build());
        bidder = userRepository.save(User.builder().username("John Buyer").email("john@example.com").password("pass").build());
    }

    @Test
    public void testAddItem() {

        Item item = Item.createItem("Test Item", "Description", 100.0, LocalDateTime.now().plusDays(1), seller);
        Long itemId = auctionService.addItem(item);

        assertThat(itemId).isNotNull();
        assertThat(itemRepository.findById(itemId)).isPresent();
        Item itemFound = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));
        assertThat(itemFound.getTitle()).isEqualTo("Test Item");
        assertThat(itemFound.getDescription()).isEqualTo("Description");
        assertThat(itemFound.getMinPrice()).isEqualTo(100.0);
        assertThat(itemFound.getDeadline()).isAfter(LocalDateTime.now());
        assertThat(itemFound.getBids()).isEmpty();

        // Verify that the item is saved correctly
        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(itemRepository.findAll().get(0).getId()).isEqualTo(itemId);

    }

    @Test
    public void testPlaceBid() {
        User seller = User.builder().username("Jane Doe").email("jane@example.com").password("pass").build();
        userRepository.save(seller);
        User bidder = User.builder().username("John Doe").email("john@example.com").password("pass").build();
        userRepository.save(bidder);

        Item item = Item.createItem("Test Item", "Description", 100.0, LocalDateTime.now().plusDays(1), seller);
        itemRepository.save(item);

        Long bidId = auctionService.placeBid(item.getId(), 150.0, bidder.getId());


        assertThat(itemRepository.findById(item.getId())).isPresent();
        assertThat(bidId).isNotNull();

        // Verify bid is saved in the repository
        assertThat(bidRepository.findById(bidId)).isPresent();
        Bid bid = bidRepository.findById(bidId).get();
        assertThat(bid.getAmount()).isEqualTo(150.0);
        assertThat(bid.getBidder()).isEqualTo(bidder);
        assertThat(bid.getItem().getId()).isEqualTo(item.getId());

        // Verify the item has the bid associated
        Item itemFound = itemRepository.findById(item.getId()).get();
        assertThat(itemFound.getBids()).hasSize(1);
        assertThat(itemFound.getBids().get(0).getId()).isEqualTo(bidId);
        assertThat(itemFound.getBids().get(0)).isEqualTo(bid);
    }

    @Test
    public void testPlaceBidOnExpiredItem() {
        Item item = Item.createItem("Expired Item", "Description", 100.0, LocalDateTime.now().minusDays(1), seller);
        itemRepository.save(item);

        assertThatThrownBy(() -> auctionService.placeBid(item.getId(), 150.0, bidder.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot place a bid on an item after its deadline.");
    }

    @Test
    public void testPlaceBidWithInsufficientAmount() {
        Item item = Item.createItem("Test Item", "Description", 100.0, LocalDateTime.now().plusDays(1), seller);
        itemRepository.save(item);

        // Attempt to place a bid lower than the minimum price
        assertThatThrownBy(() -> auctionService.placeBid(item.getId(), 50.0, bidder.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bid amount must be higher than the current highest bid.");
    }

    @Test
    @DisplayName("Test placing a bid with amount lower than the current highest bid")
    public void testPlaceBidWithInsufficientAmount2(){
        Item item = Item.createItem("Test Item", "Description", 100.0, LocalDateTime.now().plusDays(1), seller);
        itemRepository.save(item);

        // Place an initial bid
        auctionService.placeBid(item.getId(), 150.0, bidder.getId());

        // Attempt to place a lower bid
        assertThatThrownBy(() -> auctionService.placeBid(item.getId(), 140.0, bidder.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bid amount must be higher than the current highest bid.");
    }

    @Test
    public void testGetAllItems() {
        Item item = Item.createItem("Item 1", "Description 1", 100.0, LocalDateTime.now().plusDays(1), seller);
        Item item2 = Item.createItem("Item 2", "Description 2", 200.0, LocalDateTime.now().plusDays(2), seller);
        auctionService.addItem(item);
        auctionService.addItem(item2);

        var items = auctionService.getAllItems();

        assertThat(items).hasSize(2);
        assertThat(items.get(0).getTitle()).isEqualTo("Item 1");
        assertThat(items.get(1).getTitle()).isEqualTo("Item 2");
    }

    @Test
    @DisplayName("Test closing expired auctions")
    public void testCloseExpiredAuctions() {
        Item item1 = Item.createItem("Expired Item 1", "Description 1", 100.0, LocalDateTime.now().minusDays(1), seller);
        Item item2 = Item.createItem("Active Item", "Description 2", 200.0, LocalDateTime.now().plusDays(1), seller);
        auctionService.addItem(item1);
        auctionService.addItem(item2);

        // Close expired auctions
        auctionService.closeExpiredAuctions();

        // Verify that the expired item is closed
        assertThat(itemRepository.findById(item1.getId())).isPresent();
        assertThat(itemRepository.findById(item1.getId())).get()
                .extracting(Item::getStatus)
                .isEqualTo(AuctionStatus.CLOSED);
        // Verify that the active item is still open
        assertThat(itemRepository.findById(item2.getId())).isPresent();
        assertThat(itemRepository.findById(item2.getId())).get()
                .extracting(Item::getStatus)
                .isEqualTo(AuctionStatus.OPEN);
    }

    @Test
    @DisplayName("Test finalizing auction with bids")
    public void testFinalizeAuctionWithBids() {

        Item item = Item.createItem("Auction Item", "Description", 100.0, LocalDateTime.now().plusDays(1), seller);
        auctionService.addItem(item);

        // Place bids
        auctionService.placeBid(item.getId(), 150.0, bidder.getId());
        auctionService.placeBid(item.getId(), 200.0, bidder.getId());

        // Simulate the auction deadline passing
        ReflectionTestUtils.setField(item, "deadline", LocalDateTime.now().minusDays(1));

        // Close the auction
        auctionService.closeExpiredAuctions();

        // Verify that the item is closed and has a winner
        assertThat(itemRepository.findById(item.getId())).isPresent();
        Item closedItem = itemRepository.findById(item.getId()).get();
        assertThat(closedItem.getStatus()).isEqualTo(AuctionStatus.CLOSED);
        assertThat(closedItem.getWinner()).isNotNull();
        assertThat(closedItem.getWinningBid()).isNotNull();
        assertThat(closedItem.getWinningBid().getAmount()).isEqualTo(200.0);
        assertThat(closedItem.getWinner().getId()).isEqualTo(bidder.getId());
        assertThat(closedItem.getBids()).hasSize(2);
        assertThat(closedItem.getBids().get(0).getAmount()).isEqualTo(150.0);
        assertThat(closedItem.getBids().get(1).getAmount()).isEqualTo(200.0);
    }

}
