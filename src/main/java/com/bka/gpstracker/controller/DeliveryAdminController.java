package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.model.request.NewDeliveryRequest;
import com.bka.gpstracker.model.request.UpdateDeliveryRequest;
import com.bka.gpstracker.service.DeliveryAdminService;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
@Secured(AuthoritiesConstants.ROLE_ADMIN)
public class DeliveryAdminController {

    @Autowired
    private DeliveryAdminService deliveryAdminService;

    @PostMapping("/delivery")
    public ResponseEntity<DeliveryInfo> registrationDelivery(@Valid @RequestBody NewDeliveryRequest newDeliveryRequest) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(deliveryAdminService.registrationDelivery(newDeliveryRequest, currentUsername));
    }

    @GetMapping("/deliveries")
    public ResponseEntity<List<DeliveryInfo>> getDeliveries(@RequestParam(name = "created_by", required = false) String createdBy,
                                                            @RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                            @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(deliveryAdminService.getDeliveries(createdBy, pageIndex, pageSize));
    }

    @PatchMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> updateDelivery(@Valid @RequestBody UpdateDeliveryRequest updateDeliveryRequest,
                                                       @PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryAdminService.updateDelivery(updateDeliveryRequest, deliveryId));
    }


}
