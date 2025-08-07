package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
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

}
