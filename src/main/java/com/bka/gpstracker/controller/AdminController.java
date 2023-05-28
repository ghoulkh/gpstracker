package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.Authority;
import com.bka.gpstracker.model.request.PermissionRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Secured({AuthoritiesConstants.ROLE_ADMIN, AuthoritiesConstants.ROLE_ENTERPRISE_ADMIN})
    @PostMapping("/permission")
    public ResponseEntity<UserResponse> setPermission(@RequestBody @Valid PermissionRequest request) {
        return ResponseEntity.ok(adminService.addAuthorityWithUsername(request.getUsername(), Authority.Role.valueOf(request.getRole())));
    }
}
