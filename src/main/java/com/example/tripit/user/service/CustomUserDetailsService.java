package com.example.tripit.user.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (!email.matches("[^@]+@[^\\.]+\\..+")) {

            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

        }

        // DB에서 조회
        UserEntity userData = userRepository.findByEmail(email);
        logger.info("userDetail: " + email);

        if (userData != null) {
            // UserDetails에 담아서 return하면 AuthenticationManager가 검증함
            logger.info("Found user: " + userData.getUserId());

            return new CustomUserDetails(userData);

        } else if(userData == null) {
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }
        return null;
    }
}

