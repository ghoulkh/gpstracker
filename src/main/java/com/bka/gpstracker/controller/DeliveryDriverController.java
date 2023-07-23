package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.DeliveryDriverService;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class DeliveryDriverController {
    @Autowired
    private DeliveryDriverService deliveryDriverService;


    @GetMapping("/me/driver/deliveries")
    public ResponseEntity<List<DeliveryInfo>> getDeliveries(@RequestParam(name = "page_index", required = false, defaultValue = "1") int pageIndex,
                                                            @RequestParam(name = "page_size", required = false, defaultValue = "1") int pageSize,
                                                            @RequestParam(name = "status", required = false, defaultValue = "*") String status) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(deliveryDriverService.getByDriverUsernameAndStatus(currentUsername, status, pageIndex, pageSize));
    }

    @PostMapping("/driver/start-delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> driverStartDelivery(@PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryDriverService.driverStartDelivery(deliveryId));
    }


    @PostMapping("/driver/completed-delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> driverCompleteDelivery(@PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryDriverService.driverCompletedDelivery(deliveryId));
    }


    @PostMapping("/driver/cancel-delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> driverCancelDelivery(@PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryDriverService.driverCancelDelivery(deliveryId));
    }
}
