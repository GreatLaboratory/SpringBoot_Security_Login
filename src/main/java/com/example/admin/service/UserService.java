package com.example.admin.service;

import com.example.admin.dto.UserRegistrationDto;
import com.example.admin.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
