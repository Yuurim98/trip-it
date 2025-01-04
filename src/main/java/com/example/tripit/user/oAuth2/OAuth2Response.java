package com.example.tripit.user.oAuth2;


public interface OAuth2Response {
    //사용자의 정보를 응답 받는 객체 like dto


    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();

    String getGender();

    String getBirthYear();

    String getBirthday();

}
