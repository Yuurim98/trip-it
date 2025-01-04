package com.example.tripit.user.jwt;


import com.example.tripit.error.ErrorCode;
import com.example.tripit.error.ErrorResponse;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {


    private final JWTUtil jwtUtil;


    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김 (로그인이 되어있지 않았을 때)
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {

            jwtUtil.isExpired(accessToken);

        } catch (ExpiredJwtException e) {
            //토큰 만료 에러

            response.setStatus(401);

            //CustomException customException = new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ACCESS_TOKEN_EXPIRED);

//            //ObjectMapper를 사용하여 ResultResponse 객체를 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);


            //응답 본문에 JSON 작성
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.print(jsonResponse);
            writer.flush();

            //response status code
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            //프론트측과 협의된 응답코드
            //프론트측에서 토큰이 만료되었들때 400이나 401 응답을 주어서 만료 되었을때
            //리프레시 토큰을 주고 재발급 받을 수 있도록 특정한 상태코드와 응답메세지 규약


//            List<CustomException.ErrorDetail> errorDetail = Collections.singletonList(
//                    new CustomException.ErrorDetail("token", "access", "expired")
//            );
//
//            throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED, errorDetail);

            return;
        }

    // 토큰이 access인지 리프레시인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득

        //헤더에 access 토큰이 유효하다면

        String username = jwtUtil.getUsername(accessToken); //email
        String role = jwtUtil.getRole(accessToken);


        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken); //시큐리티 콘텍스터 홀더에 해당 유저 등록




        logger.info("User: " + username + " 시큐리티 콘텍스터 홀더에 해당 유저 등록 완료 ");

        filterChain.doFilter(request, response);

    }
}
