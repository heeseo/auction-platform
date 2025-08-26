package com.auction.auction.controller;

import com.auction.auction.dto.ImageRequest;
import com.auction.auction.dto.PresignedUrlResponse;
import com.auction.auction.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/presigned-urls")
    public PresignedUrlResponse getPresignedUrl(@RequestBody ImageRequest request) {
        log.info("Generating presigned URL for file: {}", request.getFilename());
        return imageService.createPresignedUrl(request.getFilename(), request.getContentType());
    }
}
