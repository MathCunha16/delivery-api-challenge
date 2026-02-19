package com.cocobambu.delivery.infrastructure;

import com.cocobambu.delivery.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InfrastructureHealthIT extends BaseIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(InfrastructureHealthIT.class);

    @Test
    @DisplayName("Smoke Test: Check if postgres are up")
    void postgresUp() {
        assertTrue(postgres.isRunning(), "Postgres container should be running");
        logger.info("Postgres is up");
    }
}
