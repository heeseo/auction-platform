package com.auction.auction.repository;

import com.auction.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT MAX(b.amount) FROM Bid b WHERE b.item.id = :itemId")
    Optional<Double> findMaxAmountByItemId(@Param("itemId") Long id);

    long countByItemId(Long itemId);

    // Custom query methods can be defined here if needed
    // For example, to find bids by item:
    // List<Bid> findByItemId(Long itemId);
}
