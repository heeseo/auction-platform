package com.auction.auction.service;

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
}
