package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.DeliveryDriverService;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class DeliveryDriverController {
    @Autowired
    private DeliveryDriverService deliveryDriverService;


    @GetMapping("/me/driver/deliveries")
    public ResponseEntity<List<DeliveryInfo>> getDeliveries() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(deliveryDriverService.getByDriverUsername(currentUsername));
    }

    @PostMapping("/driver/start-delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> driverStartDelivery(@PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryDriverService.driverStartDelivery(deliveryId));
    }


    @PostMapping("/driver/completed-delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> driverCompletedDelivery(@PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryDriverService.driverCompletedDelivery(deliveryId));
    }
}
