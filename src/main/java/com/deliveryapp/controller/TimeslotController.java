package com.deliveryapp.controller;

import com.deliveryapp.model.domain.Address;
import com.deliveryapp.service.TimeslotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class TimeslotController {

    private final TimeslotService timeslotService;

    @Autowired
    public TimeslotController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @PostMapping("/timeslots")
    public ResponseEntity<?> retrieveAllAvailableTimeslots(@RequestBody Address address) {
        try {
            return ResponseEntity.ok(timeslotService.retrieveAllAvailableTimeslots(address.getCity()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
