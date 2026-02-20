package com.cocobambu.delivery.config;

import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class LoadTestSeeder implements CommandLineRunner {

    private final StoreRepository storeRepository;

    // UUID fixo para identificar nos testes de carga
    public static final UUID GATLING_STORE_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    private final Logger logger = LoggerFactory.getLogger(LoadTestSeeder.class);

    public LoadTestSeeder(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void run(String... args) {
        if (!storeRepository.existsById(GATLING_STORE_ID)) {
            Store gatlingStore = new Store();
            gatlingStore.setId(GATLING_STORE_ID);
            gatlingStore.setName("Performance Store");
            gatlingStore.setCreatedAt(OffsetDateTime.now());

            storeRepository.save(gatlingStore);
            logger.info("Gatling Performance Store created.");
        } else {
            logger.info("Gatling Performance Store already exists.");
        }
    }
}
