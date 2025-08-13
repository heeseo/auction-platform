package com.auction.auction.service;

import com.auction.auction.model.Item;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    @DisplayName("Test adding an item to the auction")
    void testAddItem() {

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item toSave = invocation.getArgument(0);

            // Simulate DB-generated ID via reflection
            Field idField = Item.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(toSave, 1L);

            return toSave;
        });

        Long itemId = auctionService.addItem(
                "Test Item",
                "Description",
                100.0,
                LocalDateTime.now().plusDays(1)
        );

        verify(itemRepository).save(Mockito.any(Item.class));
        verifyNoMoreInteractions(itemRepository);
        verifyNoInteractions(bidRepository);

        assertThat(itemId).isEqualTo(1L);

    }

    @Test
    @DisplayName("Test placing a bid on an item")
    void testPlaceBid() throws NoSuchFieldException, IllegalAccessException {

        Item existingItem = createItemWithId(1L, "Existing Item", "Existing Description", 100.0);

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(existingItem));
        when(bidRepository.save(any())).thenAnswer(invocation -> {
            Object bid = invocation.getArgument(0);
            Field idField = bid.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bid, 1L);
            return bid;
        });

        Long bidId = auctionService.placeBid(1L, 150.0, "Bidder");

        verify(itemRepository).findById(1L);
        verify(bidRepository).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, bidRepository);

        assertThat(bidId).isEqualTo(1L);

        assertThat(existingItem.getBids()).isNotEmpty();
        assertThat(existingItem.getBids().get(0).getBidderName()).isEqualTo("Bidder");
        assertThat(existingItem.getBids().get(0).getAmount()).isEqualTo(150.0);
        assertThat(existingItem.getBids().get(0).getItem()).isEqualTo(existingItem);
    }

    @Test
    @DisplayName("Test retrieving all items")
    void testGetAllItems() {
        Item item1 = createItemWithId(1L, "Test Item", "Test Description", 100.0);
        Item item2 = createItemWithId(2L, "Another Item", "Another Description", 200.0);

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        var items = auctionService.getAllItems();

        verify(itemRepository).findAll();
        verifyNoMoreInteractions(itemRepository);
        verifyNoInteractions(bidRepository);

        assertThat(items).hasSize(2);
        assertThat(items).containsExactlyInAnyOrder(item1, item2);
    }

    @Test
    @DisplayName("Test retrieving all items when repository is empty")
    void testGetAllItemsWhenEmpty() {
        when(itemRepository.findAll()).thenReturn(List.of());

        var items = auctionService.getAllItems();

        verify(itemRepository).findAll();
        verifyNoMoreInteractions(itemRepository);
        verifyNoInteractions(bidRepository);

        assertThat(items).isEmpty();
    }

    private static Item createItemWithId(Long id, String title, String description, double minPrice) {
        Item item = new Item(title, description, minPrice, LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(item, "id", id);
        return item;
    }


}