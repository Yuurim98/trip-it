package com.example.tripit.user.service;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.tripit.error.ErrorCode;
import com.example.tripit.error.ErrorResponse;
import com.example.tripit.result.ResultCode;
import com.example.tripit.result.ResultResponse;
import com.example.tripit.user.entity.RefreshEntity;
import com.example.tripit.user.jwt.JWTUtil;
import com.example.tripit.user.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ReissueService {

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public ResponseEntity<?> reissueToken (HttpServletRequest request, HttpServletResponse response
                                     //, @CookieValue("refresh") String refresh2
    ) throws IOException {

        //서비스단을 따로 만드는것을 추천

        //get refresh token by cookie
//        String refresh = null;
//        Cookie[] cookies = request.getCookies(); //쿠키 순회 후 refresh 변수가 있는지?
//
//        for (Cookie cookie : cookies) {
//
//            if (cookie.getName().equals("refresh")) {
//
//                refresh = cookie.getValue();
//            }
//        }

        //String refreshValue = null;

        // "refresh" 헤더의 값을 가져오기
        String refresh = request.getHeader("refresh");


        if (refresh == null) {

            //response status code
//            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST); //응답은 프론트와 협업
            //ResultResponse result = ResultResponse.of(ResultCode.LOGGOUT_REQUEST,"refresh null","", "");

            log.info("refresh is null");

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGOUT_REQUEST);

            //response body
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        //expired check

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //리프레시 만료

            log.info("refresh expired");

            //response status code
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGOUT_REQUEST);

            //response body
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            log.info("not refresh");

            //response status code
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGOUT_REQUEST);

            //response body
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGOUT_REQUEST);

            log.info("not in db");

            //response body
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        //검증 완료
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);//10분
        //String newAccess = jwtUtil.createJwt("access", username, role, 10000L); //10초

        //Refresh Rotate
        //Reissue 엔트포인트에서 Refresh 토큰을 받아 Access 토큰 갱신 시 Refresh 토큰도 함께 갱신
        //주의점 : Rotate 되기 이전의 토큰을 가지고 서버측으로 가도 인증이 되기 때문에 서버측에서 발급했던 Refresh들을 기억한 뒤 블랙리스트 처리를 진행하는 로직을 작성해야 한다.
        //String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        //refreshRepository.deleteByRefresh(refresh);
        //addRefreshEntity(username, newRefresh, 86400000L);

        //response
        //response.setHeader("access", newAccess);
        //response.addCookie(createCookie("refresh", newRefresh));

        //바디로 전달

        //ResultResponse result = ResultResponse.of(ResultCode.REISSUE_SUCCESS,"",newAccess, newRefresh);
        ResultResponse result = ResultResponse.of(ResultCode.REISSUE_SUCCESS,"",newAccess, refresh, role);


        //ObjectMapper를 사용하여 ResultResponse 객체를 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(result);

        //응답 본문에 JSON 작성
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print(jsonResponse);
        writer.flush();

        log.info("reissue success");

        return new ResponseEntity<>(HttpStatus.OK);
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
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }


}
