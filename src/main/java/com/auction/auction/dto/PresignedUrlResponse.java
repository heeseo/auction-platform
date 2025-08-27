package com.auction.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponse {
    private String fileUrl;
    private String uploadUrl;
}
