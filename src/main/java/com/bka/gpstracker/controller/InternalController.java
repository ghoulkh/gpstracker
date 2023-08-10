package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.InternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {
    private final InternalService internalService;

    @PostMapping("/update/position")
    public String updatePosition(@RequestParam MultipartFile file,
                                 @RequestParam String rfid,
                                 @RequestParam String speed) throws IOException {
        internalService.importPosition(file, rfid, speed);
        return "OK";
    }
}
