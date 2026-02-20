package com.cocobambu.delivery.simulation;

import com.cocobambu.delivery.config.HttpConfig;
import com.cocobambu.delivery.feed.OrderFeeder;
import com.cocobambu.delivery.request.OrderRequests;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class OrderSimulation extends Simulation {

    private final ScenarioBuilder createOrderScn = scenario("Orders - Create")
            .feed(OrderFeeder.dynamicOrderFeeder())
            .exec(OrderRequests.createOrder)
            .pause(1);

    private final ScenarioBuilder listOrdersScn = scenario("Orders - List (paged)")
            .exec(OrderRequests.getAllOrders)
            .pause(2);

    public OrderSimulation() {

        // Total: 15s warm-up + 75s ramp + 90s steady = 180s (3 min)
        var createLoad = createOrderScn.injectOpen(
                nothingFor(Duration.ofSeconds(15)),
                rampUsersPerSec(0).to(30).during(Duration.ofSeconds(75)),
                constantUsersPerSec(30).during(Duration.ofSeconds(90))
        );

        var listLoad = listOrdersScn.injectOpen(
                nothingFor(Duration.ofSeconds(15)),
                rampUsersPerSec(0).to(10).during(Duration.ofSeconds(75)),
                constantUsersPerSec(10).during(Duration.ofSeconds(90))
        );

        setUp(createLoad, listLoad)
                .protocols(HttpConfig.HTTP_PROTOCOL)
                .assertions(
                        global().failedRequests().percent().lt(1.0),
                        global().responseTime().percentile(95).lt(800),
                        global().responseTime().percentile(99).lt(2000)
                );
    }
}