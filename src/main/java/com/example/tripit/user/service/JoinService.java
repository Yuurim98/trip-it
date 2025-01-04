package com.example.tripit.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.user.dto.JoinDTO;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder
    ) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

            String email = joinDTO.getEmail();
            String username = joinDTO.getUsername();
            String password = joinDTO.getPassword();
            String nickname = joinDTO.getNickname();
            String birth = joinDTO.getBirth();
            String gender = joinDTO.getGender();

        Boolean isEmailExist = userRepository.existsByEmail(email);
        Boolean isNicknameExist = userRepository.existsByNickname(nickname);

        if (isEmailExist) {
            //throw new InvalidInputException("email", email, "1");


            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_EXISTS
                    //,(List<CustomException.ErrorDetail>) new CustomException.ErrorDetail("","","")
                     );
        }else if(isNicknameExist){
            //throw new InvalidInputException("nickname", nickname, "2");
            throw new CustomException((ErrorCode.USER_NICKNAME_ALREADY_EXISTS));
        }


        UserEntity data = new UserEntity();

        data.setEmail(email);
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(nickname);
        data.setBirth(birth);
        data.setGender(gender);
        data.setRole("ROLE_USER");

        System.out.println((data.toString()));

        userRepository.save(data);

    }
}
