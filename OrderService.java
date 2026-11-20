package com.example.asyncdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final Executor demoExecutor;

    public OrderService(@Qualifier("demoExecutor") Executor demoExecutor) {
        this.demoExecutor = demoExecutor;
    }

    public String getFullSummary(Long userId) {
        long startTime = System.currentTimeMillis();

        // Start Task 1
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            sleep(1000); // Simulate 1 second delay
            log.info("Finished User info for userId={} on thread: {}", userId, Thread.currentThread().getName());
            return "User: John Doe";
        }, demoExecutor);

        // Start Task 2
        CompletableFuture<String> ordersFuture = CompletableFuture.supplyAsync(() -> {
            sleep(1500); // Simulate 1.5 second delay
            log.info("Finished Orders for userId={} on thread: {}", userId, Thread.currentThread().getName());
            return "Orders: 5 items";
        }, demoExecutor);

        // Start Task 3
        CompletableFuture<Integer> pointsFuture = CompletableFuture.supplyAsync(() -> {
            sleep(800); // Simulate 0.8 second delay
            log.info("Finished Points for userId={} on thread: {}", userId, Thread.currentThread().getName());
            return 120;
        }, demoExecutor);

        // Wait for ALL tasks to finish
        CompletableFuture.allOf(userFuture, ordersFuture, pointsFuture).join();

        // Get the results (join() gets the value without checked exceptions)
        String user = userFuture.join();
        String orders = ordersFuture.join();
        Integer points = pointsFuture.join();

        long totalTime = System.currentTimeMillis() - startTime;
        log.info("Total time taken: {} ms", totalTime);

        return user + " | " + orders + " | Points: " + points;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
