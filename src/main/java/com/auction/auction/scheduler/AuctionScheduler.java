package com.auction.auction.scheduler;

import com.auction.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionService auctionService;

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void closeExpiredAuctions() {
        auctionService.closeExpiredAuctions();
    }
}
