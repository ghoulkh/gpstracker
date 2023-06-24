package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.service.TripAdminService;
import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
