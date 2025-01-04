package com.example.tripit.user.oAuth2;

import com.example.tripit.user.dto.LoginDTO;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
//        else if (registrationId.equals("google")) {
//
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
//        }
        else {

            return null;
        }

        UserEntity existData = userRepository.findByEmail(oAuth2Response.getEmail());

        String nickname = "";

        if (existData == null) {

            String birth = oAuth2Response.getBirthYear() + oAuth2Response.getBirthday().replace("-", "");

            //닉네임 생성 메소드
            try {
                String uuid = UUID.randomUUID().toString();
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(uuid.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    hexString.append(String.format("%02x", b));
                }

                nickname = "user" + hexString.toString().substring(0, 10); // "user"는 4자, 해시의 앞 10자를 사용하여 총 14자

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            //USER DB에 저장

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(oAuth2Response.getName());
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setNickname(nickname);
            userEntity.setRole("ROLE_USER");
            userEntity.setBirth(birth);
            userEntity.setGender(oAuth2Response.getGender());
            userEntity.setSocialType(registrationId);

            userRepository.save(userEntity);

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setNickname(nickname);
            loginDTO.setEmail(oAuth2Response.getEmail());
            loginDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(loginDTO);

        } else {

            //데이터가 존재하는 경우 로그인으로 연결

//            existData.setEmail(oAuth2Response.getEmail());
//            existData.setUsername(oAuth2Response.getName());
//
//            userRepository.save(existData);

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setEmail(existData.getEmail());
            loginDTO.setNickname(existData.getNickname());
            loginDTO.setRole(existData.getRole());

            System.out.println( "존재하는 회원 " + existData.getEmail());

            return new CustomOAuth2User(loginDTO);
        }
    }
}
