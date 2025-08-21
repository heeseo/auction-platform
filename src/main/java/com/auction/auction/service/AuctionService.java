package com.auction.auction.service;

import com.auction.auction.model.AuctionStatus;
import com.auction.auction.model.Bid;
import com.auction.auction.model.Item;
import com.auction.auction.model.User;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
import com.auction.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuctionService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    // This class will handle the business logic for auction operations
    // such as creating auctions, placing bids, and retrieving auction data.

    @Transactional
    public Long addItem(String itemName, String description, double startingPrice, LocalDateTime deadline) {
        // Logic to add a new item to the auction
        Item item = Item.createItem(itemName, description, startingPrice, deadline, null);
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public Long addItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public Long placeBid(Long itemId, double bidAmount, Long bidderId) {
        // Logic to place a bid on an item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));
        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + bidderId));

        // Check deadline
        if (item.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot place a bid on an item after its deadline.");
        }

        // Check if the bid amount is higher than the current highest bid or minimum price
        double highestBid = item.getBids().stream()
                .mapToDouble(Bid::getAmount)
                .max()
                .orElse(item.getMinPrice());

        if (bidAmount <= highestBid) {
            throw new IllegalArgumentException("Bid amount must be higher than the current highest bid.");
        }

        Bid bid = Bid.createBid(bidAmount, LocalDateTime.now(), item, bidder);
        bidRepository.save(bid);
        return bid.getId();
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

    @Transactional
    public void closeExpiredAuctions() {
        // Logic to close auctions that have expired
        List<Item> expiredItems = itemRepository.findAllByDeadlineBeforeAndStatus(LocalDateTime.now(), AuctionStatus.OPEN);
        for (Item item : expiredItems) {
            item.changeStatus(AuctionStatus.CLOSED);
            //find the highest bid and set the winner
            finalizeAuction(item);
        }
    }

    private void finalizeAuction(Item item) {
        item.getBids().stream()
                .max(Comparator.comparingDouble(Bid::getAmount))
                .ifPresent(item::finalizeAuction);
    }
}
