package com.auction.auction.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ItemTest {

    @Test
    void testItemCreation() {
        User seller = User.builder().username("John Doe").email("john@example.com").password("pass").build();

        Item item = Item.createItem("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1), seller);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNull(); // ID should be null before persisting
        assertThat(item.getTitle()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("This is a test item.");
        assertThat(item.getMinPrice()).isEqualTo(50.0);
        assertThat(item.getDeadline()).isAfter(LocalDateTime.now());
        assertThat(item.getBids()).isEmpty();
    }

    @Test
    void testItemBidsAssociation() {
        User seller = User.builder().username("John Doe").email("john@example.com").password("pass").build();
        User bidder1 = User.builder().username("Jane Doe").email("jane@example.com").password("pass").build();
        User bidder2 = User.builder().username("Jane Doe").email("jane@example.com").password("pass").build();

        Item item = Item.createItem("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1), seller);


        Bid bid1 = Bid.createBid(100.0, LocalDateTime.now(), item, bidder1);
        Bid bid2 = Bid.createBid(150.0, LocalDateTime.now(), item, bidder2);

        assertThat(item.getBids()).hasSize(2);
        assertThat(item.getBids()).contains(bid1, bid2);
        assertThat(bid1.getItem()).isEqualTo(item);
        assertThat(bid2.getItem()).isEqualTo(item);
    }

}