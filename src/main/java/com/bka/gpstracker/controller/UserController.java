package com.bka.gpstracker.controller;

import com.bka.gpstracker.model.request.RegisterUserRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registrationUser(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registrationUser(request));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }
}
