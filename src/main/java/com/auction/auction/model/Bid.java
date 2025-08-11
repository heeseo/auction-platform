package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid {

    @Id @GeneratedValue
    @Column(name = "bid_id")
    private Long id;

    private Double amount;
    private String bidderName;
    private LocalDateTime bidTime;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Bid(double amount, String bidderName, LocalDateTime bidTime, Item item) {
        this.amount = amount;
        this.bidderName = bidderName;
        this.bidTime = bidTime;
        this.item = item;
        item.addBid(this);
    }
}
