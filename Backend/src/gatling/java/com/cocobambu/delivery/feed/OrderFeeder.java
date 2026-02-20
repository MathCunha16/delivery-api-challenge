package com.cocobambu.delivery.feed;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OrderFeeder {

    private OrderFeeder() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String DEFAULT_STORE_ID = "99999999-9999-9999-9999-999999999999"; // store do seeder

    public static Iterator<Map<String, Object>> dynamicOrderFeeder() {
        Supplier<Map<String, Object>> mapSupplier = () -> {

            int randomPhoneSuffix = ThreadLocalRandom.current().nextInt(10000000, 99999999);
            String randomPhone = "629" + randomPhoneSuffix;

            String[] names = {"Charles", "Matheus", "João", "Gustavo", "Pedro"};
            String randomName = names[ThreadLocalRandom.current().nextInt(names.length)];

            return Map.of(
                    "storeId", DEFAULT_STORE_ID,
                    "customerName", randomName,
                    "customerPhone", randomPhone,
                    "city", "Goiânia"
            );
        };

        return Stream.generate(mapSupplier).iterator();
    }
}