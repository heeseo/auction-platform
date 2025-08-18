package com.auction.auction.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String title;
    private String description;
    private Double minPrice;
    private String category;
    private String imageUrl;
    private String deadline;
    private Double currentPrice;
    private String highestBidderName;

    @Builder
    public ItemResponse(Long id, String title, String description, Double minPrice, String category,
                        String imageUrl, String deadline, Double currentPrice, String highestBidderName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.minPrice = minPrice;
        this.category = category;
        this.imageUrl = imageUrl;
        this.deadline = deadline;
        this.currentPrice = currentPrice;
        this.highestBidderName = highestBidderName;
    }
}
