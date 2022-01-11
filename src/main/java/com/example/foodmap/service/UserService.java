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

import static com.example.foodmap.exception.ErrorCode.*;
import static java.net.URLDecoder.decode;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;
//
//    region 유저 위치 저장
//    @Transactional
//    public Location saveUserLocation(UserLocationDto locationDto, User user) {
//
//        Location location = new Location(locationDto);
//        foundUser.saveLocation(location);
//
//        return foundUser.getLocation();
//    }

    public KakaoInfoResponseDto userInfo(UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        return new KakaoInfoResponseDto(
                user.getNickname(),
                user.getProfileImage(),
                user.getKakaoId(),
                user.getLevel(),
                user.getLocation()
        );
    }

    //region 로그인 후 사용자 프로필 등록
    @Transactional
    public void saverUserInfo(UserInfoRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile profileImage)   {
        //회원만 이용 가능
        if (userDetails != null) {
            User foundUser = userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND));

            String nickname = requestDto.getNickname();

            //닉네임확인(필수, 10자 이내, 중복검사)
            if (nickname ==null || nickname.trim().isEmpty()) {
                throw new CustomException(NICKNAME_EMPTY);
            }else if (nickname.length() > 10) {
                throw new CustomException(WRONG_NICKNAME_LENGTH);
            }else if (userRepository.existsByNickname(nickname)) {
                throw new CustomException(DUPLICATE_NICKNAME);
            }

            //주소 필수입력
            if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
                throw new CustomException(ADDRESS_EMPTY);
            }

            Location location = Location.builder()
                    .address(requestDto.getAddress())
                    .latitude(requestDto.getLatitude())
                    .longitude(requestDto.getLongitude())
                    .build();

            //사진(선택사항)


            String profileImagePath = "";
            if (!profileImage.isEmpty()) {
                profileImagePath = storageService.uploadFile(profileImage, "profile");
                log.info(profileImagePath);
            }

            foundUser.updateUserInfo(profileImagePath, nickname, location);

        } else {
            throw new CustomException(UNAUTHORIZED_MEMBER); //비회원 이용불가
        }
    }

    //region 사용자 프로필 수정
    @Transactional
    public UserInfoResponseDto updateUserInfo(UserInfoRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile profileImage)   {
        //회원만 이용 가능
        if (userDetails != null) {
            User foundUser = userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND));

            String nickname = requestDto.getNickname();

            //닉네임확인(10자 이내, 중복검사)
            if (nickname ==null || nickname.trim().isEmpty()) {
                nickname = foundUser.getNickname();
            } else if (nickname.length() > 10) {
                throw new CustomException(WRONG_NICKNAME_LENGTH);
            } else if (userRepository.existsByNickname(nickname)) {
                throw new CustomException(DUPLICATE_NICKNAME);
            }

            //주소확인
            Location location;
           if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
                location = Location.builder()
                        .address(requestDto.getAddress())
                        .latitude(requestDto.getLatitude())
                        .longitude(requestDto.getLongitude()).build();
            } else {
                location = foundUser.getLocation();
            }
            

            String profileImagePath ;
            if (!profileImage.isEmpty()) {
                profileImagePath = storageService.uploadFile(profileImage, "profile");
                String oldImageUrl = decode(
                        foundUser.getProfileImage().replace(
                                "https://team12-images.s3.ap-northeast-2.amazonaws.com/", ""
                        ),
                        StandardCharsets.UTF_8
                );
                storageService.deleteFile(oldImageUrl);
            } else {
                profileImagePath = foundUser.getProfileImage();
            }

            foundUser.updateUserInfo(profileImagePath, nickname, location);
            return UserInfoResponseDto.builder()
                    .nickname(nickname)
                    .profileImage(profileImagePath)
                    .location(location)
                    .build();
        } else {
            throw new CustomException(UNAUTHORIZED_MEMBER); //비회원 이용불가
        }
    }
    //endregion
}