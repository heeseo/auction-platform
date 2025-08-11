package com.auction.auction.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ItemTest {

    @Test
    void testItemCreation() {
        Item item = new Item("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1));

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
        Item item = new Item("Test Item", "This is a test item.", 50.0, LocalDateTime.now().plusDays(1));
        Bid bid1 = new Bid(100.0, "John Doe", LocalDateTime.now(), item);
        Bid bid2 = new Bid(150.0, "Jane Doe", LocalDateTime.now(), item);

        assertThat(item.getBids()).hasSize(2);
        assertThat(item.getBids()).contains(bid1, bid2);
        assertThat(bid1.getItem()).isEqualTo(item);
        assertThat(bid2.getItem()).isEqualTo(item);
    }

}