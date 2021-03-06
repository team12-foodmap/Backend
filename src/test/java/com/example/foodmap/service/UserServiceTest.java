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
        location = new Location("?????????",123.231,12.234);
        user1 = new User(
                "?????????",
                "asdf1234",
                222L,
                "hanghae99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "?????????"
        );
        User user2 = new User(
                "????????????",
                "ZLKE4",
                142L,
                "hange99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "?????????"
        );
        user3 = new User(
                "?????????",
                "ZLKE4",
                442L,
                "hane99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "?????????"
        );



        nickname = "?????????";
        latitude=123.231;
        longitude = 12.234;
        address ="?????????";
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
    @DisplayName("????????????")
    void ????????????() {
        //given

        //when
        KakaoInfoResponseDto kakaoInfoResponseDto = userService.userInfo(userDetails01);

        assertThat(user1.getNickname()).isEqualTo(kakaoInfoResponseDto.getNickname());
        assertThat(user1.getKakaoId()).isEqualTo(kakaoInfoResponseDto.getKakaoId());
        assertThat(user1.getId()).isEqualTo(kakaoInfoResponseDto.getUserId());

    }

    @Test
    @DisplayName("??????-???????????????")
    void ???????????????() {
        //given

        //when
        userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("?????????");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }



    @Test
    @DisplayName("??????-???????????????")
    void ???????????????1() {
        // given
        nickname="?????????";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("?????? ???????????? ??????????????????.");

    }
    @Test
    @DisplayName("??????-?????????null")
    void ???????????????2() {


        userInfoRequestDto = new UserInfoRequestDto(
                null,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("???????????? ??????????????????.");
    }

    @Test
    @DisplayName("??????-????????? ??????")
    void ???????????????3() {

        nickname="aljstlajstwlwl";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("???????????? 10??? ????????? ??????????????????.");
    }
    @Test
    @DisplayName("??????-??????")
    void ???????????????4() {

        address="";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("??????????????? ??????????????????.");

    }
    @Test
    @DisplayName("??????-?????????")
    void ???????????????5() {



        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.saverUserInfo(userInfoRequestDto,userDetails02,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("????????? ????????? ?????? ??? ????????????.");
    }


    @Test
    @DisplayName("??????-???????????????")
    void ???????????????() {
        //given

        //when
        userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("?????????");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }

    @Test
    @DisplayName("??????-?????? ????????? ???????????????")
    void ???????????????01() {
        //given
        nickname = "?????????";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        //when
        userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);

        //then
        Optional<User> user = userRepository.findByUsername(user1.getUsername());

        assertThat(user.get().getNickname()).isEqualTo("?????????");
        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
    }


    @Test
    @DisplayName("??????-???????????????")
    void ???????????????1() {
        // given
        nickname="?????????";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("?????? ???????????? ??????????????????.");

    }


    @Test
    @DisplayName("??????-????????? ??????")
    void ???????????????3() {

        nickname="aljstlajstwlwl";
        userInfoRequestDto = new UserInfoRequestDto(
                nickname,latitude,longitude,address);
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails01,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("???????????? 10??? ????????? ??????????????????.");
    }

    @Test
    @DisplayName("??????-?????????")
    void ???????????????5() {



        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(userInfoRequestDto,userDetails02,multipartFile);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("????????? ????????? ?????? ??? ????????????.");
    }


}