package com.auction.auction.service;

import com.auction.auction.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Service s3Service;

    public PresignedUrlResponse createPresignedUrl(String filename, String contentType) {
        String key = UUID.randomUUID() + "_" + filename;
        String uploadUrl = s3Service.generatePresignedUrl(key, contentType);
        String fileUrl = s3Service.getPublicUrl(key);
        return new PresignedUrlResponse(fileUrl, uploadUrl);
    }
}
