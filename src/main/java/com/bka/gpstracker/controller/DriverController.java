package com.bka.gpstracker.controller;

import com.bka.gpstracker.model.request.ChangeStatusTripRequest;
import com.bka.gpstracker.model.response.PositionResponse;
import com.bka.gpstracker.service.DriverService;
import com.bka.gpstracker.solr.entity.Position;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @PostMapping("/driver/change-trip")
    public ResponseEntity<Trip> changeStatus(@RequestBody @Valid ChangeStatusTripRequest request) {
        return ResponseEntity.ok(driverService.changeStatusTrip(request.getTripId(), request.getStatus()));
    }

    @GetMapping("/driver/trips")
    public ResponseEntity<List<Trip>> getAllByDriver() {
        String currentUsername = SecurityUtil.getCurrentUsername();
         return ResponseEntity.ok(driverService.getAllTripByDriver(currentUsername));
    }

    @GetMapping("/driver/{username}/position-log")
    public ResponseEntity<List<PositionResponse>> getAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok(driverService.getByUsername(username));
    }

}
