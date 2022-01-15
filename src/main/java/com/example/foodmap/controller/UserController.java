package com.example.foodmap.controller;

import com.example.foodmap.dto.user.KakaoInfoResponseDto;
import com.example.foodmap.dto.user.KakaoUserResponseDto;
import com.example.foodmap.dto.user.UserInfoRequestDto;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.KakaoUserService;
import com.example.foodmap.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final KakaoUserService kakaoUserService;
    private final UserService userService;

    @GetMapping("/user/kakao/callback")
    public ResponseEntity<KakaoUserResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        KakaoUserResponseDto kakaoUserResponseDto = kakaoUserService.kakaoLogin(code,response);
        return ResponseEntity.ok().body(kakaoUserResponseDto);
    }

    //유저정보
    @GetMapping("/userInfo")
    public KakaoInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.userInfo(userDetails);
    }

    //첫 로그인 시 프로필 등록
    @PutMapping("/userInfo/save")
    public ResponseEntity<?> saveUserInfo(
            @ModelAttribute UserInfoRequestDto requestDto,
            @RequestParam MultipartFile profileImage,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        userService.saverUserInfo(requestDto, userDetails,profileImage);
        return ResponseEntity.ok().body("사용자 정보가 등록되었습니다.");
    }

    //사용자 프로필수정
    @PutMapping("/userInfo/update")
    public ResponseEntity<?> updateUserInfo(
            @ModelAttribute UserInfoRequestDto requestDto,
            @RequestParam MultipartFile profileImage,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )   {

        return ResponseEntity.ok().body(userService.updateUserInfo(requestDto, userDetails,profileImage));
    }
}
