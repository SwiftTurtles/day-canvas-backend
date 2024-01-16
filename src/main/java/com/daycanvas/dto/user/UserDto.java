package com.daycanvas.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {

    private Long id;
    private String email;
    private String nickname;
    private String provider; //사용자가 로그인한 서비스 ex) google, kakao, naver

}
