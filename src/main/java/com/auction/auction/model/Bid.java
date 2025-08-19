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
    private LocalDateTime bidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User bidder;

    protected Bid(double amount, LocalDateTime bidTime, Item item, User bidder) {
        this.amount = amount;
        this.bidTime = bidTime;
        this.item = item;
        this.bidder = bidder;
    }

    public static Bid createBid(double amount, LocalDateTime bidTime, Item item, User bidder) {
        Bid bid = new Bid(amount, bidTime, item, bidder);
        item.addBid(bid);
        bidder.addBid(bid);
        return bid;
    }

    public void setItem(Item item) {
        this.item = item;
        item.addBid(this);
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
        bidder.addBid(this);
    }
}
