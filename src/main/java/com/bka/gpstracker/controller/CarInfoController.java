package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.model.response.Response;
import com.bka.gpstracker.service.CarInfoService;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class CarInfoController {

    @Autowired
    private CarInfoService carInfoService;

    @GetMapping("/cars-info")
    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    public ResponseEntity<List<CarInfo>> getCarsInfo(@RequestParam(name = "page_index", required = false, defaultValue = "1") int pageIndex,
                                                     @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(carInfoService.getCarsInfo(pageIndex, pageSize));
    }

    @GetMapping("/me/cars-info")
    public ResponseEntity<List<CarInfo>> getCarInfo() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(carInfoService.getByUsername(currentUsername));
    }

    @PostMapping("/test/check_in/{rfid}")
    public ResponseEntity<Response> testCheckIn(@PathVariable String rfid) {
        carInfoService.testCheckIn(rfid);
        return ResponseEntity.ok(new Response());
    }

}
