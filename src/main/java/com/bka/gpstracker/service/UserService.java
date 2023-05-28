package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.Authority;
import com.bka.gpstracker.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.RegisterUserRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.repository.*;
import com.bka.gpstracker.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private EnterpriseService enterpriseService;


    public UserResponse registrationUser(RegisterUserRequest request) {
        if (userInfoRepository.findById(request.getUsername()).isPresent())
            throw new TrackerAppException(ErrorCode.CONFLICT_USERNAME);
        UserInfo userInfoToSave = request.toUser();
        userInfoToSave.setPassword(passwordEncoder.encode(request.getPassword()));

        validateEnterpriseCode(userInfoToSave);
        UserInfo result = userInfoRepository.save(userInfoToSave);
        userRepository.save(userInfoToSave.toUser());

        Authority authority = new Authority();
        authority.setUserInfo(userInfoToSave);
        authority.setRole(Authority.Role.ROLE_USER);
        authorityRepository.save(authority);

        return UserResponse.from(result);
    }

    private void validateEnterpriseCode(UserInfo userInfoToSave) {
        if (userInfoToSave.getEnterpriseCode() == null)
            return;
        if (!enterpriseService.findByEnterpriseCode(userInfoToSave.getEnterpriseCode()).isPresent())
            throw new TrackerAppException(ErrorCode.ENTERPRISE_CODE_INVALID);
    }

    public UserInfo getByUsername(String username) {
        return userInfoRepository.findById(username).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));
    }

    public Optional<UserInfo> findByUsername(String username) {
        return userInfoRepository.findById(username);
    }

    public UserResponse getProfile() {
        String currentUser = SecurityUtil.getCurrentUsername();
        UserInfo userInfo = userInfoRepository.findById(currentUser).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(userInfo);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = userInfoRepository.findById(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        List<String> roles = user.get().getAuthorities().stream().map(authority ->
                authority.getRole().getValue()).collect(Collectors.toList());

        if (roles.isEmpty()) {
            roles.add("ROLE_USER");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword() != null ? user.get().getPassword() : "", getAuthorities(roles));
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<String> roles) {
        List<SimpleGrantedAuthority> result =  new ArrayList<>();
        for (int i = 0; i < roles.size(); i ++) {
            result.add(new SimpleGrantedAuthority(roles.get(i)));
        }
        return result;
    }
}
