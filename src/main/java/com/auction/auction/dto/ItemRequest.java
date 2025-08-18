package com.auction.auction.dto;

import lombok.Data;

@Data
public class ItemRequest {

    private String title;
    private String description;
    private Double minPrice;
    private String category;
    private String imageUrl;
    private String deadline;

}
