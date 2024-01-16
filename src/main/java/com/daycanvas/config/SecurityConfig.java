package com.daycanvas.config;

import com.daycanvas.domain.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/", "/login").permitAll() // 해당 URL은 인증 절차 수행 생략 가능.antMatchers("/posts/**")
                .anyRequest().authenticated()
                .and()
//                .logout()
//                .logoutSuccessUrl("/LogoutSuccess")   // @Todo 추후 home 으로 변경
//                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2Service)
                .and()
                .defaultSuccessUrl("/posts")
                .and().build();
    }
}
