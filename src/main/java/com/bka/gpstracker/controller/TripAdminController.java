package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.model.request.SetDriverForTripRequest;
import com.bka.gpstracker.model.response.Response;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.service.TripAdminService;
import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
public class TripAdminController {

    @Autowired
    private TripAdminService tripAdminService;

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @GetMapping("/admin/trips")
    public ResponseEntity<List<Trip>> getActiveTrips(@RequestParam(required = false, defaultValue = "*") String status) {
        return ResponseEntity.ok(tripAdminService.getActiveTrips(status));
    }

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @GetMapping("/admin/drivers")
    public ResponseEntity<List<UserResponse>> getAllUsernameDriver() {
        return ResponseEntity.ok(tripAdminService.getAllUsernameDriver());
    }

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @PostMapping("/admin/set-driver")
    public ResponseEntity<Response> setDriverForTrip(@RequestBody @Valid SetDriverForTripRequest setDriverForTripRequest) {
        tripAdminService.setDriverForTrip(setDriverForTripRequest.getDriver(), setDriverForTripRequest.getTripId());
        return ResponseEntity.ok(new Response());
    }

}
