package com.auction.auction.service;

import com.auction.auction.model.Bid;
import com.auction.auction.model.Item;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
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

    // This class will handle the business logic for auction operations
    // such as creating auctions, placing bids, and retrieving auction data.

    @Transactional
    public Long addItem(String itemName, String description, double startingPrice, LocalDateTime deadline) {
        // Logic to add a new item to the auction
        Item item = new Item(itemName, description, startingPrice, deadline);
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public Long placeBid(Long itemId, double bidAmount, String bidderName) {
        // Logic to place a bid on an item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        Bid bid = new Bid(bidAmount, bidderName, LocalDateTime.now(), item);
        bidRepository.save(bid);
        return bid.getId();
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

}
