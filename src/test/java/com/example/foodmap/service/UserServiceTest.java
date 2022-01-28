package com.example.foodmap.service;


import com.example.foodmap.config.EmbeddedS3Config;
import com.example.foodmap.dto.user.KakaoInfoResponseDto;
import com.example.foodmap.dto.user.UserInfoRequestDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.User;
import com.example.foodmap.model.UserRoleEnum;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@Rollback
@ActiveProfiles("test")
@Import(EmbeddedS3Config.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    S3Mock s3Mock;
    @Autowired
    StorageService storageService;



    private MockMultipartFile multipartFile;
    private UserDetailsImpl userDetails01;
    private UserDetailsImpl userDetails02;
    private User user1;
    private User user3;
    private Location location;
    private UserInfoRequestDto userInfoRequestDto;
    private String nickname;
    private double latitude;
    private double longitude;
    private String address;


    @BeforeEach
    void setup(){
        location = new Location("강남구",123.231,12.234);
        user1 = new User(
                "파이리",
                "asdf1234",
                222L,
                "hanghae99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "파이리"
        );
        User user2 = new User(
                "어니부기",
                "ZLKE4",
                142L,
                "hange99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "코끼리"
        );
        user3 = new User(
                "거북왕",
                "ZLKE4",
                442L,
                "hane99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "거북왕"
        );



        nickname = "꼬부기";
        latitude=123.231;
        longitude = 12.234;
        address ="용산역";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);

        String expected = "gksdnf.png";
        multipartFile = new MockMultipartFile("file", expected,
                "image/png", "test data".getBytes());

        userDetails01 = new UserDetailsImpl(user1);
        userDetails02 = new UserDetailsImpl(user2);
        userRepository.save(user1);
        userRepository.save(user3);
    }

    @Test
    @DisplayName("유저정보")
    void 유저정보() {
        //given

        //when
        KakaoInfoResponseDto kakaoInfoResponseDto = userService.userInfo(userDetails01);

        assertThat(user1.getNickname()).isEqualTo(kakaoInfoResponseDto.getNickname());
        assertThat(user1.getKakaoId()).isEqualTo(kakaoInfoResponseDto.getKakaoId());
        assertThat(user1.getId()).isEqualTo(kakaoInfoResponseDto.getUserId());

    }

    @Test
    @DisplayName("성공-프로필저장")
    void 프로필저장() {
        //given

        //when
        userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("꼬부기");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }



    @Test
    @DisplayName("실패-닉네임중복")
    void 프로필저장1() {
        // given
        nickname="파이리";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("이미 존재하는 닉네임입니다.");

    }
    @Test
    @DisplayName("실패-닉네임null")
    void 프로필저장2() {


        userInfoRequestDto = new UserInfoRequestDto(
                null,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("닉네임을 입력해주세요.");
    }

    @Test
    @DisplayName("실패-닉네임 길이")
    void 프로필저장3() {

        nickname="aljstlajstwlwl";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("닉네임은 10자 이내로 입력해주세요.");
    }
    @Test
    @DisplayName("실패-주소")
    void 프로필저장4() {

        address="";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("관심주소를 입력해주세요.");

    }
    @Test
    @DisplayName("실패-비회원")
    void 프로필저장5() {



        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails02,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
    }


    @Test
    @DisplayName("성공-프로필수정")
    void 프로필수정() {
        //given

        //when
        userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("꼬부기");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }

    @Test
    @DisplayName("성공-같은 닉네임 프로필수정")
    void 프로필수정01() {
        //given
        nickname = "파이리";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        //when
        userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("파이리");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }


    @Test
    @DisplayName("실패-닉네임중복")
    void 프로필수정1() {
        // given
        nickname="거북왕";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("이미 존재하는 닉네임입니다.");

    }


    @Test
    @DisplayName("실패-닉네임 길이")
    void 프로필수정3() {

        nickname="aljstlajstwlwl";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("닉네임은 10자 이내로 입력해주세요.");
    }

    @Test
    @DisplayName("실패-비회원")
    void 프로필수정5() {



        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails02,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
    }


}