package com.auction.auction.mapper;

import com.auction.auction.dto.ItemRequest;
import com.auction.auction.dto.ItemResponse;
import com.auction.auction.model.Item;

import java.time.LocalDateTime;

public class ItemMapper {

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .minPrice(item.getMinPrice())
                .category(null)      // null 가능
                .imageUrl(null)      // null 가능
                .deadline(String.valueOf(item.getDeadline()))
                .currentPrice(null)
                .build();
    }

    public static Item toEntity(ItemRequest itemRequest) {
        return new Item(
                itemRequest.getTitle(),
                itemRequest.getDescription(),
                itemRequest.getMinPrice(),
                LocalDateTime.parse(itemRequest.getDeadline())
        );
    }
}
