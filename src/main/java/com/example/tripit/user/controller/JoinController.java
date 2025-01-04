package com.example.tripit.user.controller;

import com.example.tripit.user.dto.JoinDTO;
import com.example.tripit.user.service.JoinService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> joinProcessJson(@RequestBody JoinDTO joinDTO) {
        
    	System.out.println("회원가입시도");
    	joinService.joinProcess(joinDTO);
        return ResponseEntity.ok("회원가입이 성공적으로 완료 되었습니다. ");
    }


    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> joinProcessForm(@ModelAttribute JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        System.out.println("회원가입시도");
        return ResponseEntity.ok("회원가입이 성공적으로 완료 되었습니다. ");
    }

}
