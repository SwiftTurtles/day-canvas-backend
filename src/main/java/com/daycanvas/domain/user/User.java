package com.daycanvas.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "nickname", nullable = true)
    private String nickname;

    @Column(name = "provider")
    private String provider; //사용자가 로그인한 서비스 ex) google, kakao, naver ...etc

    public User updateUser(String email, String nickname, String provider) {
        this.setEmail(email);
        this.setProvider(provider);
        return this;
    }
}
