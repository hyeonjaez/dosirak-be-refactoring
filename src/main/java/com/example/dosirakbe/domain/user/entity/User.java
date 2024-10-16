package com.example.dosirakbe.domain.user.entity;


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

    @Column(name = "user_name",updatable = false)
    private String userName;

    @Column(name = "nick_name",unique = true)
    private String nickName;

    @Column(name = "email",unique = true,updatable = false)
    private String email;

    @Column(name = "created_at",updatable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "user_valid")
    private boolean userValid;


}
