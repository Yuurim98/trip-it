package com.example.tripit.user.jwt;

import com.example.tripit.user.entity.RefreshEntity;
import com.example.tripit.user.repository.RefreshRepository;
import com.example.tripit.result.ResultCode;
import com.example.tripit.result.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class  LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String email = request.getParameter("email"); //고유한 식별자
        String password = obtainPassword(request);


        System.out.println("로그인 시도 " + email);

        //인증 토큰 생성
        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);


        //token에 담은 검증을 위한 AuthenticationManager로 전달
        // 사용자 자격 증명을 검증하고, 인증된 Authentication 객체를 반환한다
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        //유저 정보
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        //60000L = 1분
        String access = jwtUtil.createJwt("access", email, role, 6000000L);
        String refresh = jwtUtil.createJwt("refresh", email, role, 86400000L);//24시간

        //Refresh 토큰 DB에 저장
        addRefreshEntity(email, refresh, 86400000L);


        //응답설정
        //response.setHeader("access", access); //프론트단에서 로컬 스토리지에 저장해두고 쓰면 됌
        // response.addCookie(createCookie("refresh", refresh)); //쿠키에 저장

//        String setCookie = "";
//
//        if (response.containsHeader("Set-Cookie")) {
//            setCookie = response.getHeader("Set-Cookie");
//            logger.info("Set-Cookie: " + setCookie);
//        } else {
//            logger.info("Set-Cookie header not found");
//        }
        response.setStatus(HttpStatus.OK.value());
        ResultResponse result = ResultResponse.of(ResultCode.LOGIN_SUCCESS,email, access, refresh,
                role);


        //ObjectMapper를 사용하여 ResultResponse 객체를 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(result);

        //응답 본문에 JSON 작성
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print(jsonResponse);
        writer.flush();

        logger.info("토큰 :" + access);
        logger.info("refresh :" + refresh);

        //로그 추가: 응답 헤더 확인
        response.getHeaderNames().forEach(headerName -> {
            logger.info(headerName + ": " + response.getHeader(headerName));
        });
    }

    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        //로그인 실패시 401 응답 코드 반환

        response.setStatus(401);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print("error");
        writer.flush();

    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); //생명주기
        //cookie.setHttpOnly(true);
        //cookie.setSecure(true); //https 통신을 할 경우
        //cookie.setDomain("172.16.1.184");
        cookie.setPath("/"); //쿠키의 범위
        //cookie.setAttribute("SameSite","None");
        return cookie;
    }
}


