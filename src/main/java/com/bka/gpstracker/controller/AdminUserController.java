package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.model.request.AuthorCarInfoRequest;
import com.bka.gpstracker.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/user/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @PostMapping("/car-info/author")
    public ResponseEntity<CarInfo> setCarInfoAuthor(@RequestBody @Valid AuthorCarInfoRequest request) {
        return ResponseEntity.ok(adminUserService.setCarAuthor(request.getUsername(), request.getRfid()));
    }
}
