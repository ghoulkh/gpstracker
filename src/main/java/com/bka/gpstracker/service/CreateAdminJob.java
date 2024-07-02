package com.bka.gpstracker.service;

import com.bka.gpstracker.model.request.RegisterUserRequest;
import com.bka.gpstracker.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Log4j2
public class CreateAdminJob {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;

    @Value("${app.admin.username}")
    private String usernameAdmin;
    @Value("${app.admin.password}")
    private String passwordAdmin;

    @PostConstruct
    private void registerAdmin() {
        if (userService.findByUsername(this.usernameAdmin).isPresent()) {
            return;
        }
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("admin@gmail.com");
        request.setPhone("0826662323");
        request.setFullName("ADMIN");
        request.setPassword(this.passwordAdmin);
        request.setUsername(this.usernameAdmin);
        userService.registrationUser(request);

        adminService.registerRoleAdmin(this.usernameAdmin);
        log.info("Register admin done!");
    }
}
