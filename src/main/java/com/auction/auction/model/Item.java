package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.Bidi;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String title;
    private String description;
    private Double minPrice;
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    public Item(String title, String description, double minPrice, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.minPrice = minPrice;
        this.deadline = deadline;
    }

    public int addBid(Bid bid) {
        this.bids.add(bid);
        return this.bids.size();
    }
}
