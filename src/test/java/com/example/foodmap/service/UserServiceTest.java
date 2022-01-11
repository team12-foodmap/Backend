//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.user.UserLocationDto;
//import com.example.foodmap.model.Location;
//import com.example.foodmap.model.User;
//import com.example.foodmap.model.UserRoleEnum;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//
//    private UserDetailsImpl userDetails01;
//    private User user1;
//    @Test
//    @DisplayName("사용자 주소 저장 - 성공")
//    void saveLocation() {
//
//        //given
//        user1 = User.builder()
//                .id(1L)
//                .username("김주란")
//                .kakaoId(1L)
//                .email("test@naver.com")
//                .level(1L)
//                .password("password")
//                .role(UserRoleEnum.USER)
//                .build();
//
//         UserLocationDto userLocation = UserLocationDto.builder()
//                .address("전북 익산시 부송동 100")
//                .latitude(127.1163593869371)
//                .longitude(127.1163593869371)
//                .build();
//
//         when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
//
//        //when
//        Location result = userService.saveUserLocation(userLocation, user1);
//        //then
//        assertThat(result.getAddress()).isEqualTo("전북 익산시 부송동 100");
//        assertThat(result.getLatitude()).isEqualTo(127.1163593869371);
//        assertThat(result.getLongitude()).isEqualTo(127.1163593869371);
//    }
//
//}