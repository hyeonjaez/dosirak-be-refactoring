package com.example.dosirakbe.global.user.entity;


import jakarta.persistence.*;
import lombok.*;


@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED) //기본생성자
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",unique = true,updatable = false)
    private Long userId;

    @Column(name = "user_name",unique = true,updatable = false)
    private Long userName;

    @Column(name = "nick_name",unique = true,updatable = false)
    private Long nickName;

    @Column(name = "email",unique = true,updatable = false)
    private Long email;

    @Column(name = "created_at",unique = true,updatable = false)
    private Long createdAt;

    @Column(name = "updated_at",unique = true,updatable = false)
    private Long updatedAt;

    @Column(name = "profile_img",unique = true,updatable = false)
    private Long profileImg;

    @Column(name = "user_valid",unique = true,updatable = false)
    private Long userValid;

}
