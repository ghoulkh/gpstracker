package com.bka.gpstracker.controller;

import com.bka.gpstracker.model.request.NewTripRequest;
import com.bka.gpstracker.model.response.Response;
import com.bka.gpstracker.service.BookCarService;
import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
public class BookCarController extends BaseController {

    @Autowired
    private BookCarService bookCarService;

    @PostMapping("/trip")
    public ResponseEntity<Trip> registerNewTrip(@RequestBody @Valid NewTripRequest request) {
        return ResponseEntity.ok(bookCarService.newTrip(request));
    }

    @PutMapping("/trip/{tripId}/cancel")
    public ResponseEntity<Response> cancelTrip(@PathVariable String tripId) {
        bookCarService.cancelTrip(tripId);
        return makeResponseOk();
    }

    @GetMapping("/me/trips")
    public ResponseEntity<List<Trip>> getAllTrip() {
        return ResponseEntity.ok(bookCarService.getAllTrip());
    }
}
