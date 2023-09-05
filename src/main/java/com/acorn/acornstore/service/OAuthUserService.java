package com.acorn.acornstore.service;

import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.domain.repository.UserRepository;
import com.acorn.acornstore.web.dto.OAuth2UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OAuthUserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    public OAuthUserService() {
        super();
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // DefaultOAuth2UserService의 기존 loadUser를 호출한다. 이 메서드가 user-info-uri를 이용해 사용자 정보를 가져오는 부분이다.
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            // 디버깅을 돕기 위해 사용자 정보가 어떻게 되는지 로깅한다. 테스팅 시에만 사용해야 한다.
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // login 필드를 가져온다.
        final String username = (String) oAuth2User.getAttributes().get("login");
        final String authProvider = userRequest.getClientRegistration().getClientName();
        User user = null;
        Optional<User> userOptional = userRepository.findByEmail(username);
        // 유저가 존재하지 않으면 새로 생성한다.
        if(!userRepository.existsByEmail(username)) {
            user = user.builder()
                    .email(username)
                    .authProvider(authProvider)
                    .build();
            user = userRepository.save(user);
        } else {
//                user = userRepository.findByEmail(username);
            user = userOptional.get();
        }

        log.info("Successfully pulled user info username {} authProvider {}",
                username,
                authProvider);
        // 변경 부분
        return new OAuth2UserDTO(user.getId(), oAuth2User.getAttributes());
    }

}
