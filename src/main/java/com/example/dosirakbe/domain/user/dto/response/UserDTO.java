package com.example.dosirakbe.domain.user.dto.response;

import com.example.dosirakbe.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String userName;
    private String name;
    private String email;
    private String profileImg;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImg = user.getProfileImg();
    }


}
