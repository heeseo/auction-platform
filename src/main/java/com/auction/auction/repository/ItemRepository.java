package com.auction.auction.repository;

import com.auction.auction.model.AuctionStatus;
import com.auction.auction.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByDeadlineBeforeAndStatus(LocalDateTime now, AuctionStatus status);

}
