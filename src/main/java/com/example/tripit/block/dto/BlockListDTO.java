package com.example.tripit.block.dto;

import java.time.LocalDateTime;

import com.example.tripit.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class BlockListDTO {

    private Long blockId;
    private UserDTO userId;
    private String nickname;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime blockDate;
}
