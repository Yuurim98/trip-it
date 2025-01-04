package com.example.tripit.user.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity (name = "REFRESHTOKEN")
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "refresh", nullable = false, length = 500)
    private String refresh;

    @Column(name = "expiration", nullable = false, length = 50)
    private String expiration;

    private String email;

}