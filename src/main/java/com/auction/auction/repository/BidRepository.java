package com.auction.auction.repository;

import com.auction.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find bids by item:
    // List<Bid> findByItemId(Long itemId);
}
