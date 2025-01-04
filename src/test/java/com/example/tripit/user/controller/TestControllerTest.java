package com.example.tripit.user.controller;

import org.junit.jupiter.api.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityContextHolderTest {

    private String username;

    private String password;

    private String USER_ROLE;

    @BeforeAll
    void setup() {
        username = "abcdef@naver.com";
        password = "12345a!!";
        USER_ROLE = "ROLE_USER";
    }

    @Test
    @Order(1)
    void 인증된_사용자정보를_셋팅한다() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new TestingAuthenticationToken(username, password, USER_ROLE);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    @Test
    @Order(2)
    void 인증된_사용자정보에_접근한다() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        assertThat(authentication.isAuthenticated(), is(true));
        assertThat(authentication.getName(), is(username));
        assertThat(authentication.getCredentials(), is(password));
        assertThat(authentication.getAuthorities(), containsInAnyOrder(
                hasProperty("authority", is(USER_ROLE))));
    }
}