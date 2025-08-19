package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    protected Item(String title, String description, double minPrice, LocalDateTime deadline, User seller) {
        this.title = title;
        this.description = description;
        this.minPrice = minPrice;
        this.deadline = deadline;
        this.seller = seller;
    }

    public static Item createItem(String title, String description, double minPrice, LocalDateTime deadline, User seller) {
        Item item = new Item(title, description, minPrice, deadline, seller);
        item.setSeller(seller);
        return item;
    }

    public void setSeller(User seller) {
        this.seller = seller;
        seller.addItemForSale(this);
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
    }
}
