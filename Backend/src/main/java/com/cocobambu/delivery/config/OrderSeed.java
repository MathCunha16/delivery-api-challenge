package com.cocobambu.delivery.config;

import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

@Configuration
@Profile("!test")
public class OrderSeed {

    private final Logger logger = LoggerFactory.getLogger(OrderSeed.class);

    @Bean
    CommandLineRunner initDatabase(
            OrderRepository orderRepository,
            StoreRepository storeRepository,
            JsonMapper jsonMapper,
            OrderMapper mapper
    ) {
        return args -> {
            if (orderRepository.count() > 0) {
                logger.info("Database already populated. Skipping seed.");
                return;
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/seeds/pedidos.json")) {
                if (inputStream == null) {
                    logger.error("Seed file not found: /seeds/pedidos.json");
                    return;
                }

                List<OrderWrapperResponse> wrappers = jsonMapper.readValue(
                        inputStream,
                        new TypeReference<List<OrderWrapperResponse>>() {}
                );

                for (OrderWrapperResponse wrapper : wrappers) {
                    Order orderEntity = mapper.toEntityFromResponse(wrapper.order());

                    Store store = orderEntity.getStore();
                    if (store != null && store.getId() != null) {
                        Store managed = storeRepository.findById(store.getId())
                                .orElseGet(() -> storeRepository.save(store));
                        orderEntity.setStore(managed);
                    }

                    orderRepository.save(orderEntity);
                }

                logger.info("Seeding finished. Inserted {} orders.", wrappers.size());
            } catch (Exception e) {
                logger.error("Error seeding database.", e);
            }
        };
    }
}