package com.daycanvas.domain.oauth;

import com.daycanvas.dto.user.UserDto;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) -> {
        UserDto userDto = new UserDto();
        userDto.setEmail((String) attribute.get("email"));
        return userDto;
    }),

    NAVER("naver", (attribute) -> {
        UserDto userDto = new UserDto();

        // 네이버의 경우 'response' 속성에서 사용자 정보를 가져옴
        Map<String, String> responseValue = (Map)attribute.get("response");
        userDto.setEmail(responseValue.get("email"));

        return userDto;
    }),

    KAKAO("kakao", (attribute) -> {
        UserDto userDto = new UserDto();
        userDto.setNickname("DayCanvas");   //email이 없기 때문에 default nickname을 지정해준다.

        return userDto;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, UserDto> of; // 로그인한 사용자의 정보를 통하여 UserDto를 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserDto> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserDto extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
