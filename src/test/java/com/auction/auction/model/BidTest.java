package com.auction.auction.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class BidTest {

    @Test
    void testBidCreation() {
        User seller = User.builder().username("sel ler").email("seller@example.com").password("pass").build();

        Item item = Item.createItem("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1), seller);
        User user = User.builder().username("John Doe").email("john@example.com").password("pass").build();
        Bid bid = Bid.createBid(100.0, LocalDateTime.now(), item, user);

        assertThat(bid).isNotNull();
        assertThat(bid.getId()).isNull(); // ID should be null before persisting
        assertThat(bid.getAmount()).isEqualTo(100.0);
        assertThat(bid.getBidder().getUsername()).isEqualTo("John Doe");
        assertThat(bid.getBidTime()).isNotNull();
        assertThat(bid.getItem()).isEqualTo(item);
    }

    @Test
    void testBidItemAssociation() {
        User seller = User.builder().username("sel ler").email("seller@example.com").password("pass").build();

        Item item = Item.createItem("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1), seller);
        User user1 = User.builder().username("Jane Doe").email("jane@example.com").password("pass").build();
        User user2 = User.builder().username("John Doe").email("john@example.com").password("pass").build();
        Bid bid = Bid.createBid(100.0, LocalDateTime.now(), item, user1);
        Bid bid2 = Bid.createBid(200.0, LocalDateTime.now(), item, user2);

        assertThat(item).isEqualTo(bid.getItem());
        assertThat(item).isEqualTo(bid2.getItem());
        assertThat(item.getBids()).hasSize(2);
        assertThat(item.getBids()).containsExactly(bid, bid2);
    }

}