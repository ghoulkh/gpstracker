package com.bka.gpstracker.controller;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.solr.entity.Authority;
import com.bka.gpstracker.model.request.PermissionRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @PostMapping("/permission")
    public ResponseEntity<UserResponse> setPermission(@RequestBody @Valid PermissionRequest request) {
        return ResponseEntity.ok(adminService.addAuthorityWithUsername(request.getUsername(), Authority.Role.valueOf(request.getRole())));
    }

    @Secured(AuthoritiesConstants.ROLE_ADMIN)
    @GetMapping("/user-info")
    public ResponseEntity<List<UserResponse>> getAllUser(@RequestParam(name = "user_name", defaultValue = "*", required = false) String username,
                                                         @RequestParam(name = "page_index", defaultValue = "1", required = false) int pageIndex,
                                                         @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(adminService.getUserAndPaging(username, pageIndex, pageSize));
    }
}
