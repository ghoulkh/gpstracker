package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.DeliveryService;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/delivery/{deliveryId}/email-receiver/{emailReceiver}")
    public ResponseEntity<DeliveryInfo> getByDeliveryIdAndEmailReceiver(@PathVariable String deliveryId,
                                                                        @PathVariable String emailReceiver) {
        return ResponseEntity.ok(deliveryService.getByDeliveryIdAndEmailOrPhone(deliveryId, emailReceiver));
    }
}
