package com.example.tripit.user.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tripit.user.service.ReissueService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@Controller
@ResponseBody //RestController + Contoller + ResponseBody
public class ReissueController {
    //jwt를 받아서 검증을 하고 새로운 jwt 발급

    private final ReissueService reissueService;


    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response
            //, @CookieValue("refresh") String refresh2
    ) throws IOException {
        return reissueService.reissueToken(request, response);
    }
}
