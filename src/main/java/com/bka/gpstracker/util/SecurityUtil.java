package com.bka.gpstracker.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityUtil {
    public static String getCurrentUsername() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            return username;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isAuthor(String... role) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                List<String> roles = ((UserDetails) principal).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                for (int i = 0; i < role.length; i ++) {
                    if (roles.contains(role[i]))
                        return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
