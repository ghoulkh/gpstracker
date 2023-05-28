package com.bka.gpstracker.controller;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @RequestMapping("/position/{rfid}")
    public ResponseEntity<List<PositionLog>> getByRfid(@PathVariable String rfid,
                                                       @RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                       @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(positionService.getByRfid(rfid, pageIndex, pageSize));
    }
}
