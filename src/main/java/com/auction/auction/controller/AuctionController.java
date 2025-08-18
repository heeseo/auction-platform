package com.auction.auction.controller;

import com.auction.auction.dto.ItemRequest;
import com.auction.auction.dto.ItemResponse;
import com.auction.auction.mapper.ItemMapper;
import com.auction.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info(auctionService.getAllItems().toString());
        // This endpoint retrieves all items available in the auction.
        return auctionService.getAllItems().stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/items/add")
    public Long addItem(@RequestBody ItemRequest itemRequest) {
        log.info("Adding new item to auction: {}", itemRequest);

        return auctionService.addItem(ItemMapper.toEntity(itemRequest));
    }

}
