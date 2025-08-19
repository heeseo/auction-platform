package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Item> itemsForSale = new ArrayList<>();
    @OneToMany(mappedBy = "bidder", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    public void addItemForSale(Item item) {
        this.itemsForSale.add(item);
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
    }
}
