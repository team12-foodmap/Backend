//package com.example.foodmap.service;
//
//import com.example.foodmap.config.EmbeddedS3Config;
//import com.example.foodmap.dto.user.KakaoInfoResponseDto;
//import com.example.foodmap.dto.user.UserInfoRequestDto;
//import com.example.foodmap.dto.user.UserLocationDto;
//import com.example.foodmap.exception.CustomException;
//import com.example.foodmap.model.Location;
//import com.example.foodmap.model.User;
//import com.example.foodmap.model.UserRoleEnum;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import io.findify.s3mock.S3Mock;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//
//@Transactional
//@Rollback
//@Import(EmbeddedS3Config.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    S3Mock s3Mock;
//
//    @Autowired
//    StorageService storageService;
//
//    private MockMultipartFile multipartFile;
//    private UserDetailsImpl userDetails01;
//    private User user1;
//    private Location location;
//    private UserInfoRequestDto userInfoRequestDto;
//
//    @BeforeEach
//    void setup(){
//        location = new Location("강남구",123.231,12.234);
//        user1 = new User(
//                "파이리",
//                "asdf1234",
//                222L,
//                "hanghae99@naver.com",
//                UserRoleEnum.USER,
//                1L,
//                "http://sljlet.com",
//                location,
//                "파이리"
//        );
//        userInfoRequestDto = new UserInfoRequestDto(
//                "꼬부기",
//                123.231,
//                12.234,
//                "용산역");
//
//        String expected = "mock1.png";
//        multipartFile = new MockMultipartFile("file", expected,
//                "image/png", "test data".getBytes());
//
//        userDetails01 = new UserDetailsImpl(user1);
//        userRepository.save(user1);
//    }
//
//    @Test
//    @DisplayName("유저정보")
//    void 유저정보() {
//        //given
//
//        //when
//        KakaoInfoResponseDto kakaoInfoResponseDto = userService.userInfo(userDetails01);
//
//        assertThat(user1.getNickname()).isEqualTo(kakaoInfoResponseDto.getNickname());
//        assertThat(user1.getKakaoId()).isEqualTo(kakaoInfoResponseDto.getKakaoId());
//        assertThat(user1.getId()).isEqualTo(kakaoInfoResponseDto.getUserId());
//
//    }
//
//    @Test
//    @DisplayName("성공-프로필저장")
//    void 프로필저장() {
//        //given
//
//        //when
//        userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
//
//        //then
//        Optional<User> user = userRepository.findByUsername(user1.getUsername());
//
//        assertThat(user.get().getNickname()).isEqualTo("꼬부기");
//        assertThat(user.get().getNickname()).isEqualTo(user1.getNickname());
//    }
//    @Test
//    @DisplayName("실패-닉네임중복")
//    void 프로필저장1() {
//        // given
//        userInfoRequestDto = new UserInfoRequestDto(
//                "파이리",
//                123.231,
//                12.234,
//                "용산역");
//
//        Exception exception = assertThrows(CustomException.class, () -> {
//            userService.saverUserInfo(userInfoRequestDto,userDetails01,multipartFile);
//        });
//
//        //then
//        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");
//
//
//    }
//
//
//
//}