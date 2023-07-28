package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCity() {
        return ResponseEntity.ok(addressService.getAllCity());
    }

    @GetMapping("/district")
    public ResponseEntity<List<String>> getDistrictByCity(@RequestParam(required = false, name = "city", defaultValue = "") String city) {
        return ResponseEntity.ok(addressService.getDistrictByCity(city));
    }

    @GetMapping("/street")
    public ResponseEntity<List<String>> getStressByDistrict(@RequestParam(required = false, name = "district", defaultValue = "")String district) {
        return ResponseEntity.ok(addressService.getStreetByDistrict(district));
    }
}