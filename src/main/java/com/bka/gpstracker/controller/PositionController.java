package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.model.request.PositionLogRequest;
import com.bka.gpstracker.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @RequestMapping("/positions/{rfid}")
    public ResponseEntity<List<PositionLog>> getByRfid(@PathVariable String rfid,
                                                       @RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                       @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(positionService.getByRfid(rfid, pageIndex, pageSize));
    }


    @PostMapping("/position")
    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    public ResponseEntity<PositionLog> addPosition(@RequestBody @Valid PositionLogRequest request) {
        return ResponseEntity.ok(positionService.addPositionLog(request));
    }
}
