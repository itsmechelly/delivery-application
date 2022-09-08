package com.deliveryapp.controller;

import com.deliveryapp.model.dto.delivery.create.CreateDeliveryDto;
import com.deliveryapp.service.DeliveryService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final Bucket bucket;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
        Bandwidth limit = Bandwidth.classic(2, Refill.greedy(2, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/deliveries")
    public ResponseEntity<?> bookDelivery(@RequestBody CreateDeliveryDto createDeliveryDto) {
        try {
            if (bucket.tryConsume(1)) {
                return ResponseEntity.ok(deliveryService.bookDelivery(createDeliveryDto));
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deliveries/{deliveryId}/complete")
    public ResponseEntity<?> completeDelivery(@PathVariable Long deliveryId) {
        try {
            return ResponseEntity.ok(deliveryService.completeDelivery(deliveryId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deliveries/{deliveryId}")
    public ResponseEntity<?> cancelDelivery(@PathVariable Long deliveryId) {
        try {
            return ResponseEntity.ok(deliveryService.cancelDelivery(deliveryId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/deliveries/daily")
    public ResponseEntity<?> getAllDailyDeliveries() {
        try {
            return ResponseEntity.ok(deliveryService.getAllDailyDeliveries());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/deliveries/weekly")
    public ResponseEntity<?> getAllWeeklyDeliveries() {
        try {
            return ResponseEntity.ok(deliveryService.getAllWeeklyDeliveries());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
