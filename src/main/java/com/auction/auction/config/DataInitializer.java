package com.auction.auction.config;

import com.auction.auction.model.Bid;
import com.auction.auction.model.Item;
import com.auction.auction.model.User;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ItemRepository;
import com.auction.auction.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Slf4j
public class DataInitializer {




    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initDb();
    }


    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final UserRepository userRepository;
        private final ItemRepository itemRepository;
        private final PasswordEncoder passwordEncoder;
        private final BidRepository bidRepository;

        public void initDb() {
            createSampleUsers();
            createSampleItems();
            createSampleBids();
        }

        private void createSampleBids() {
            if (bidRepository.count() == 0) {
                User bidder1 = userRepository.findByUsername("user2").orElseThrow();
                Item item1 = itemRepository.findAll().get(0);
                Item item2 = itemRepository.findAll().get(1);
                Item item3 = itemRepository.findAll().get(2);
                Item item4 = itemRepository.findAll().get(3);


                Bid bid1 = Bid.createBid(40.0, Instant.now(), item1, bidder1);
                Bid bid2 = Bid.createBid(110.0, Instant.now(), item2, bidder1);

                bidRepository.save(bid1);
                bidRepository.save(bid2);

                log.info("Sample bids created on items: {}, {}", item1.getTitle(), item2.getTitle());
            }
        }

        private void createSampleItems() {
            if (itemRepository.count() == 0) {
                // generate sample items
                User seller = userRepository.findByUsername("user1").orElseThrow();

                Item item1 = Item.builder()
                        .title("Vintage Chair")
                        .description("In mint condition")
                        .minPrice(30.0)
                        .deadline(Instant.now().plusSeconds(86400)) // 24시간 후 마감
                        .seller(seller)
                        .imageUrl("https://auction-platform-uploads.s3.eu-west-1.amazonaws.com/a27de0c6-5527-45fa-a696-bc3c19c28c27_img-2.png")
                        .build();

                Item item2 = Item.builder()
                        .title("Antique Clock")
                        .description("Works perfectly, very old piece")
                        .minPrice(100.0)
                        .deadline(Instant.now().plusSeconds(172800)) // 48시간 후 마감
                        .seller(seller)
                        .imageUrl("https://auction-platform-uploads.s3.eu-west-1.amazonaws.com/2001193782_01.jpg")
                        .build();

                Item item3 = Item.builder()
                        .title("Used Monitor")
                        .description("Good condition, minor scratches")
                        .minPrice(50.0)
                        .deadline(Instant.now().plusSeconds(259200)) // 72시간 후 마감
                        .seller(seller)
                        .imageUrl("https://auction-platform-uploads.s3.eu-west-1.amazonaws.com/27-inch-monitor-2048px-233796-3x2-1-1.jpg")
                        .build();

                Item item4 = Item.builder()
                        .title("Collectible Toy")
                        .description("Limited edition collectible toy.")
                        .minPrice(75.0)
                        .deadline(Instant.now().plusSeconds(360000)) // 100시간 후 마감
                        .seller(seller)
                        .imageUrl("https://auction-platform-uploads.s3.eu-west-1.amazonaws.com/kaws.jpg")
                        .build();

                itemRepository.save(item1);
                itemRepository.save(item2);
                itemRepository.save(item3);
                itemRepository.save(item4);

                log.info("Sample items created: {}, {}, {}, {}", item1.getTitle(), item2.getTitle(), item3.getTitle(), item4.getTitle());
            }
        }

        private void createSampleUsers() {
            if (userRepository.count() == 0) {
                //generate sample users
                User user1 = User.builder()
                        .username("user1")
                        .email("user1@example.com")
                        .password(passwordEncoder.encode("securepass"))
                        .build();
                userRepository.save(user1);

                User user2 = User.builder()
                        .username("user2")
                        .email("user2@example.com")
                        .password(passwordEncoder.encode("securepass"))
                        .build();
                userRepository.save(user2);

                log.info("Sample users created: {}, {}", user1.getUsername(), user2.getUsername());
            }
        }

    }

}