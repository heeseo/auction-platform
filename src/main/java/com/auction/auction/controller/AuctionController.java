package com.auction.auction.controller;

import com.auction.auction.dto.ItemRequest;
import com.auction.auction.dto.ItemResponse;
import com.auction.auction.mapper.ItemMapper;
import com.auction.auction.model.Item;
import com.auction.auction.model.User;
import com.auction.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/items")
    public List<ItemResponse> getAllItems() {
        log.info("Fetching all auction items");
        // This endpoint retrieves all items available in the auction.
        return auctionService.getAllItems()
                .stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/items")
    public Long addItem(@RequestBody ItemRequest itemRequest, @AuthenticationPrincipal User user) {
        log.info("Adding new item to auction: {}", itemRequest);
        Item item = ItemMapper.toEntity(itemRequest, user);
        return auctionService.addItem(item);
    }

    @GetMapping("/items/{id}")
    public ItemResponse getItemById(@PathVariable Long id) {
        log.info("Fetching item with id: {}", id);
        Item item = auctionService.getItemById(id);
        return ItemMapper.toResponse(item);
    }

    @PostMapping("/items/{id}/bids")
    public Long placeBid(@PathVariable Long id, @RequestParam double bidAmount, @AuthenticationPrincipal User user) {
        log.info("Placing bid on item with id: {}, bidAmount: {}", id, bidAmount);
        log.info("Placing bid by user: {}, {}", user.getId(), user.getUsername());
        // allows users to place a bid on an item in the auction.
        return auctionService.placeBid(id, bidAmount, user.getId());
    }
}
