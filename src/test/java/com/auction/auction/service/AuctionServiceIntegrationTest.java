package com.auction.auction.service;

import com.auction.auction.model.Bid;
import com.auction.auction.model.Item;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    AuctionService auctionService;

    @Test
    public void testAddItem() {

        Long itemId = auctionService.addItem(
                "Test Item",
                "Description",
                100.0,
                LocalDateTime.now().plusDays(1)
        );

        assertThat(itemId).isNotNull();
        assertThat(itemRepository.findById(itemId)).isPresent();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));
        assertThat(item.getTitle()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Description");
        assertThat(item.getMinPrice()).isEqualTo(100.0);
        assertThat(item.getDeadline()).isAfter(LocalDateTime.now());
        assertThat(item.getBids()).isEmpty();

        // Verify that the item is saved correctly
        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(itemRepository.findAll().get(0).getId()).isEqualTo(itemId);

    }

    @Test
    public void testPlaceBid() {
        Long itemId = auctionService.addItem(
                "Test Item",
                "Description",
                100.0,
                LocalDateTime.now().plusDays(1)
        );
        Long bidId = auctionService.placeBid(itemId, 150.0, "Bidder1");


        assertThat(bidId).isNotNull();

        // Verify bid is saved in the repository
        assertThat(bidRepository.findById(bidId)).isPresent();
        Bid bid = bidRepository.findById(bidId).get();
        assertThat(bid.getAmount()).isEqualTo(150.0);
        assertThat(bid.getBidderName()).isEqualTo("Bidder1");
        assertThat(bid.getItem().getId()).isEqualTo(itemId);

        // Verify the item has the bid associated
        Item item = itemRepository.findById(itemId).get();
        assertThat(item.getBids()).hasSize(1);
        assertThat(item.getBids().get(0).getId()).isEqualTo(bidId);
        assertThat(item.getBids().get(0)).isEqualTo(bid);
    }

    @Test
    public void testGetAllItems() {
        auctionService.addItem("Item 1", "Description 1", 100.0, LocalDateTime.now().plusDays(1));
        auctionService.addItem("Item 2", "Description 2", 200.0, LocalDateTime.now().plusDays(2));

        var items = auctionService.getAllItems();

        assertThat(items).hasSize(2);
        assertThat(items.get(0).getTitle()).isEqualTo("Item 1");
        assertThat(items.get(1).getTitle()).isEqualTo("Item 2");
    }

}
