package com.bka.gpstracker.controller;

import com.bka.gpstracker.service.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api")
public class FileController {
    @Autowired
    private FTPService ftpService;

    @GetMapping("/files")
    public ResponseEntity<List<String>> getAllPath() {
        return ResponseEntity.ok(ftpService.getAllPathFile());
    }


    @GetMapping(value = "/image/{directory}/{filePrefix}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String directory, @PathVariable String filePrefix) {
        return ftpService.getImageFromPath(directory + "/" + filePrefix);
    }
}
