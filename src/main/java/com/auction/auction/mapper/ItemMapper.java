package com.auction.auction.mapper;

import com.auction.auction.dto.ItemRequest;
import com.auction.auction.dto.ItemResponse;
import com.auction.auction.dto.ItemWithCurrentPrice;
import com.auction.auction.model.Item;
import com.auction.auction.model.User;

import java.time.Instant;
import java.time.LocalDateTime;

public class ItemMapper {

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .minPrice(item.getMinPrice())
                .category(null)      // null 가능
                .imageUrl(item.getImageUrl())      // null 가능
                .deadline(String.valueOf(item.getDeadline()))
                .currentPrice(null)
                .build();
    }

    public static ItemResponse toResponse(ItemWithCurrentPrice dto) {
        Item item = dto.getItem();
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .minPrice(item.getMinPrice())
                .category(null)      // null 가능
                .imageUrl(item.getImageUrl())      // null 가능
                .deadline(String.valueOf(item.getDeadline()))
                .currentPrice(dto.getCurrentPrice())
                .bidCount(dto.getBidCount())
                .build();
    }

    public static Item toEntity(ItemRequest itemRequest, User user) {
        return Item.builder()
                .title(itemRequest.getTitle())
                .description(itemRequest.getDescription())
                .minPrice(itemRequest.getMinPrice())
                .deadline(Instant.parse(itemRequest.getDeadline()))
                .seller(user)
                .imageUrl(itemRequest.getImageUrl())
                .build();
    }
}
