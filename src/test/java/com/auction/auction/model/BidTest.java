package com.auction.auction.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class BidTest {

    @Test
    void testBidCreation() {
        Item item = new Item("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1));
        Bid bid = new Bid(100.0, "John Doe", LocalDateTime.now(), item);

        assertThat(bid).isNotNull();
        assertThat(bid.getId()).isNull(); // ID should be null before persisting
        assertThat(bid.getAmount()).isEqualTo(100.0);
        assertThat(bid.getBidderName()).isEqualTo("John Doe");
        assertThat(bid.getBidTime()).isNotNull();
        assertThat(bid.getItem()).isEqualTo(item);
    }

    @Test
    void testBidItemAssociation() {
        Item item = new Item("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1));
        Bid bid = new Bid(100.0, "Jane Doe", LocalDateTime.now(), item);
        Bid bid2 = new Bid(200.0, "John Doe", LocalDateTime.now(), item);


        assertThat(item).isEqualTo(bid.getItem());
        assertThat(item).isEqualTo(bid2.getItem());
        assertThat(item.getBids()).hasSize(2);
        assertThat(item.getBids()).containsExactly(bid, bid2);

    }
}