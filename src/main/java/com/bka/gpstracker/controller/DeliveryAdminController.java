package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.model.request.NewDeliveryRequest;
import com.bka.gpstracker.model.request.UpdateDeliveryRequest;
import com.bka.gpstracker.model.response.DeliveryInfoResponse;
import com.bka.gpstracker.model.response.PositionResponse;
import com.bka.gpstracker.service.DeliveryAdminService;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/api")
@Slf4j
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
    public ResponseEntity<List<DeliveryInfo>> getDeliveries(@RequestParam(name = "created_by", required = false, defaultValue = "*") String createdBy,
                                                            @RequestParam(name = "driver", required = false, defaultValue = "*") String driver,
                                                            @RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                            @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(deliveryAdminService.getDeliveries(createdBy, driver, pageIndex, pageSize));
    }

    @GetMapping("/deliveries/canceled")
    public ResponseEntity<List<DeliveryInfo>> getDeliveriesCanceled(@RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                                    @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(deliveryAdminService.getAllDeliveryCanceled(pageIndex, pageSize));
    }

    @PatchMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryInfo> updateDelivery(@Valid @RequestBody UpdateDeliveryRequest updateDeliveryRequest,
                                                       @PathVariable String deliveryId) {
        return ResponseEntity.ok(deliveryAdminService.updateDelivery(updateDeliveryRequest, deliveryId));
    }

    @GetMapping("/deliveries/excel")
    public ResponseEntity<byte[]> getByTime(@RequestParam(name = "start_time", required = false) Long startTime,
                                            @RequestParam(name = "end_time", required = false) Long endTime) {

        Path path = deliveryAdminService.getByTime(startTime, endTime);

        try {
            byte[] excelContent = Files.readAllBytes(path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = path.getFileName().toString();
            headers.setContentDispositionFormData("attachment", fileName + ".xlsx");

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("ERROR getByTime: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
