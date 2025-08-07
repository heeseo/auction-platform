package com.auction.auction.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.text.Bidi;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

}
