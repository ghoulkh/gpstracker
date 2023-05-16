package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.User;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.RegisterUserRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.repository.UserRepository;
import com.bka.gpstracker.util.SecurityUtil;
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

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registrationUser(RegisterUserRequest request) {
        if (userRepository.findById(request.getUsername()).isPresent())
            throw new TrackerAppException(ErrorCode.CONFLICT_USERNAME);
        User userToSave = request.toUser();
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));

        User result = userRepository.save(userToSave);
        return UserResponse.from(result);
    }

    public User getByUsername(String username) {
        return userRepository.findById(username).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }

    public UserResponse getProfile() {
        String currentUser = SecurityUtil.getCurrentUsername();
        User user = userRepository.findById(currentUser).orElseThrow(() ->
                new TrackerAppException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword() != null ? user.get().getPassword() : "", roles);
    }
}
