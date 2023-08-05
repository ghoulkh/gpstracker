package com.bka.gpstracker.service;

import com.bka.gpstracker.config.JwtToken;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.ChangePasswordRequest;
import com.bka.gpstracker.model.request.LoginRequest;
import com.bka.gpstracker.model.request.ResetPasswordRequest;
import com.bka.gpstracker.solr.entity.CodeForgotPass;
import com.bka.gpstracker.solr.entity.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private CodeForgotPassService codeForgotPassService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private CarInfoService carInfoService;


    public void changePassword(ChangePasswordRequest request) {
        UserInfo userToChange = userService.getByUsername(request.getUsername());
        authenticate(request.getUsername(), request.getOldPassword());
        userToChange.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(userToChange);
    }

    public void forgotPasswordByUsername(String username) {
        UserInfo user = userService.getByUsername(username);
        CodeForgotPass codeForgotPass = codeForgotPassService.generateCode(username);
        emailService.sendMailForgotPassword(codeForgotPass.getCode(), user.getEmail(), username);
    }

    public void resetPassword(ResetPasswordRequest request) {
        UserInfo userToChange = userService.getByUsername(request.getUsername());
        codeForgotPassService.validateCode(request.getUsername(), request.getCode());
        userToChange.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(userToChange);
    }

    public String validateUsernamePasswordAndGenToken(LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return jwtToken.generateToken(loginRequest.getUsername());
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new TrackerAppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    public boolean checkPermissionConnectToSocket(String token, String rfid) {
        try {
            String username = jwtToken.validateToken(token);
            return carInfoService.isAuthorCar(username, rfid);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }
}
