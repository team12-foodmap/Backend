package com.example.foodmap.service;

import com.example.foodmap.dto.user.KakaoInfoResponseDto;
import com.example.foodmap.dto.user.UserInfoRequestDto;
import com.example.foodmap.dto.user.UserInfoResponseDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.example.foodmap.exception.ErrorCode.*;
import static java.net.URLDecoder.decode;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public KakaoInfoResponseDto userInfo(UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        return new KakaoInfoResponseDto(
                user.getNickname(),
                user.getProfileImage().trim().isEmpty()? "" : StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + user.getProfileImage(),
                user.getKakaoId(),
                user.getId(),
                user.getLevel(),
                user.getLocation()
        );
    }

    //region 로그인 후 사용자 프로필 등록
    @Transactional
    public void saverUserInfo(UserInfoRequestDto requestDto, UserDetailsImpl userDetails, MultipartFile profileImage)   {

        //회원만 이용 가능
        if (userDetails != null) {
            User foundUser = userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND));

            String nickname = requestDto.getNickname();

            //닉네임확인(필수, 10자 이내, 중복검사)
            if (nickname == null || nickname.trim().isEmpty()) {
                throw new CustomException(NICKNAME_EMPTY);
            }else if (nickname.length() > 10) {
                throw new CustomException(WRONG_NICKNAME_LENGTH);
            }else if (userRepository.existsByNickname(nickname)) {
                throw new CustomException(DUPLICATE_NICKNAME);
            }

            //주소 필수입력
            if (requestDto.getAddress().isBlank()) {
                throw new CustomException(ADDRESS_EMPTY);
            }
            //사진(선택사항)
            String imagePath = "";

            if (profileImage != null) {
                imagePath = storageService.uploadFile(profileImage, "profile");
            } else {
                imagePath = "";
            }

            foundUser.updateUserInfo(imagePath, nickname, new Location(requestDto));

        } else {
            throw new CustomException(UNAUTHORIZED_MEMBER); //비회원 이용불가
        }
    }

    //region 사용자 프로필 수정(요청값은 기존 사용자 정보)
    @Transactional
    public UserInfoResponseDto updateUserInfo(UserInfoRequestDto requestDto, UserDetailsImpl userDetails, MultipartFile profileImage)   {
        //회원만 이용 가능
        if (userDetails != null) {
            User foundUser = userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND));

           String nickname = requestDto.getNickname();

            //닉네임확인(10자 이내, 중복검사(사용자가 현재 사용하고 있는 닉네임과 같은 닉네임 등록시에는 중복되어도 상관없음)
            if (nickname.isEmpty()) {
                nickname = foundUser.getNickname();
            } else if (nickname.length() > 10) {
                throw new CustomException(WRONG_NICKNAME_LENGTH);
            } else if(!Objects.equals(nickname, foundUser.getNickname()) && userRepository.existsByNickname(nickname)){
                throw new CustomException(DUPLICATE_NICKNAME);
            }

            //주소확인
            Location location = foundUser.getLocation();
            if(!Objects.equals(requestDto.getAddress(), foundUser.getLocation().getAddress()))  {
                location = new Location(requestDto);
            }

            //사진확인
            String imagePath = foundUser.getProfileImage();
            if (profileImage != null) {
                imagePath = storageService.updateFile(imagePath, profileImage, "profile");
            } else {
                imagePath = foundUser.getProfileImage();
            }

            foundUser.updateUserInfo(imagePath, nickname, location);
            return UserInfoResponseDto.builder()
                    .nickname(nickname)
                    .profileImage(imagePath)
                    .location(location)
                    .build();
        } else {
            throw new CustomException(UNAUTHORIZED_MEMBER); //비회원 이용불가
        }
    }
    //endregion
}