package com.example.tripit.user.oAuth2;

import com.example.tripit.user.dto.LoginDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    //Spring Security 에는 들어갈 수 있는 객체 타입이 정해져 있기 떄문에
    //OAuth2User을 상속받아서 구현 해줘야 한다.
    //소셜로그인으로 로그인 한 사용자 정보


    private final LoginDTO loginDTO;

    public CustomOAuth2User(LoginDTO loginDTO) {

        this.loginDTO = loginDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return loginDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() { // Nickname

        return loginDTO.getNickname();
    }

    public String getUsername() { //email

        return loginDTO.getEmail();
    }
}
