package com.auction.auction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BidRequest {
    @NotNull
    @Positive
    private Double bidAmount;
}
