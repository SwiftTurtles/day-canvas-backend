package com.daycanvas.domain.oauth;

import com.daycanvas.domain.user.User;
import com.daycanvas.domain.user.UserRepository;
import com.daycanvas.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //DefaultOAuth2User 서비스를 통해 User 정보를 가져와야함 -> 대리자 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 로그인을 수행한 서비스의 이름 (Google/Naver/Kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 로그인 진행시 키가 되는 필드값 == PK
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // PK가 되는 정보

        Map<String, Object> attributes = oAuth2User.getAttributes(); // 사용자가 가지고 있는 정보

        UserDto userDto = OAuthAttributes.extract(registrationId, attributes);
        userDto.setProvider(registrationId);
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            userDto.setNickname(extractNickname(userDto.getEmail()));
        }
        updateOrSaveUser(userDto);

        Map<String, Object> customAttribute =
                getCustomAttribute(registrationId, userNameAttributeName, attributes, userDto);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName);
    }

    /**
     * Attribute를 가져오는 커스텀 메소드
     * @param registrationId 로그인 수행한 서비스 이름
     * @param userNameAttributeName 로그인 진행시 키가 되는 필드값. PK
     */
    public Map getCustomAttribute(String registrationId,
                                  String userNameAttributeName,
                                  Map<String, Object> attributes,
                                  UserDto userDto) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            customAttribute.put("email", userDto.getEmail());
        }

        return customAttribute;
    }

    /**
     * UserDto 필드 값 set하고 저장하여 저장된 User 객체 반환
     * @param userDto UserDto
     * @return User
     */
    public User updateOrSaveUser(UserDto userDto) {
        User user = userRepository
                .findByEmailAndProvider(userDto.getEmail(), userDto.getProvider())
                .map(value ->
                        value.updateUser(userDto.getEmail(), userDto.getProvider(), userDto.getNickname())
                )
                .orElse(modelMapper.map(userDto, User.class));

        return userRepository.save(user);
    }

    /**
     * @param email Email
     * @return @이전 문자 추출
     */
    private static String extractNickname(String email) {
        // 이메일 주소에서 '@' 이전의 문자열 추출
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            // '@'이 없을 경우 기본 NickName 설정
            return "DayCanvas"; //
        }
    }

}
