package com.auction.auction.dto;

import com.auction.auction.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemWithCurrentPrice {
    private final Item item;
    private final Double currentPrice;
}
