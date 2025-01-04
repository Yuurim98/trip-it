package com.example.tripit.user.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_Id")
    private Long userId;

    private String email;
    private String username;
    private String nickname;
    @JsonIgnore
    private String password; // 직렬화에서 제외
    private String birth;
    private String gender;

    private String intro;

    private String role;

    @Temporal(TemporalType.TIMESTAMP) //JPA 엔티티 클래스의 필드가 데이터베이스에 어떤 시간 정보로 매핑되는지를 지정
    @Column(name="regdate")
    private Date regdate;

    @PrePersist //어노테이션은 엔티티가 영속화되기 전에 실행되는 메서드를 지정
    public void prePersist() {
        this.regdate = new Date();
    }
    //영속화란 객체를 데이터베이스가 이해할 수 있는 형태로 변환하고 저장하는 것.

    @Column(name="social_type")
    private String socialType;

    private String userpic;

    
    
//    //신고횟수
    @Column(name="report_count")
    private int reportCount;
    
//    @Column(name="end_date")
//    private Date endDate;
//	
    
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> roles = new HashSet<>();

}
