package com.example.foodmap.service;

import com.example.foodmap.dto.user.KakaoUserRequestDto;
import com.example.foodmap.dto.user.KakaoUserResponseDto;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.User;
import com.example.foodmap.model.UserRoleEnum;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.security.jwt.JwtTokenUtils;
import com.example.foodmap.security.provider.JWTAuthProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTAuthProvider jwtAuthProvider;


    public KakaoUserResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserRequestDto kakaoUserRequestDto = getKakaoUserInfo(accessToken);


// 3. "카카오 사용자 정보"로 필요시 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserRequestDto);

// 4. 강제 로그인 처리
        return forceLogin(kakaoUser, response);

    }

    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP Body 생성//
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
//        body.add("client_id", "ca4aaaf3918b25c2334089d9a1d1e8ce");
        body.add("redirect_uri", "https://www.everybokki.com/user/kakao/callback");
        body.add("client_id", "c0e66cf5516530d16e6aa5105af69ae9");
//        body.add("client_id", "b70f4e2d805f84002174ac1aa5b2f11a");
//        body.add("client_id", "bb12684de559c620bf0153229d1621cb");
//        body.add("client_id", "96226a61dfa74ab382d1603dde61b318");
//        body.add("redirect_uri", "http://localhost:3000/user/kakao/callback");
        body.add("code", code);


// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserRequestDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        Long id = jsonNode.get("id").asLong();
        String email;
        if(jsonNode.get("kakao_account").get("has_email").asBoolean(false)&&
                jsonNode.get("kakao_account").get("email_needs_agreement").asBoolean(true)){
            email="";
        }else {
            email = jsonNode.get("kakao_account")
                .get("email").asText();
        }
        String profileImage = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();


        return new KakaoUserRequestDto(id, nickname, email, profileImage);
    }

    private User registerKakaoUserIfNeeded(KakaoUserRequestDto kakaoUserRequestDto) {
// DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserRequestDto.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
// 회원가입

// username: kakao nickname
            String username = kakaoUserRequestDto.getUsername();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

// email: kakao email
            String email = kakaoUserRequestDto.getEmail();
            String profileImage = kakaoUserRequestDto.getProfileImage();
            //레벨 계산해야함
            Long level = 1L;
            String nickname = "";
// role: 일반 사용자
            UserRoleEnum role = UserRoleEnum.USER;
            Location location = new Location();
            kakaoUser = new User(username, encodedPassword, kakaoId, email, role, level, profileImage, location, nickname);
            userRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

    //강제로그인
    private KakaoUserResponseDto forceLogin(User kakaoUser, HttpServletResponse response) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //JWT토큰 헤더에 생성
        String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader("Authorization", "Bearer " + token);


        if (kakaoUser.getNickname().equals("")) {
            boolean result = true;
            return KakaoUserResponseDto.builder()
                    .JWtToken(token)
                    .result(result)
                    .build();

        } else {
            boolean result = false;
            return KakaoUserResponseDto.builder()
                    .JWtToken(token)
                    .result(result)
                    .build();
        }

    }


}
