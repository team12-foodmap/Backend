package com.example.foodmap.repository;

import com.example.foodmap.model.Location;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.User;
import com.example.foodmap.model.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest
//@ActiveProfiles("test")
class RestaurantRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    UserRepository userRepository;


    User testUser;
    Restaurant restaurant1;
    Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        // Mock 테스트 유져 생성
        testUser = User.builder()
                .nickname("라이언은귀여워")
                .username("김주란")
                .kakaoId(102L)
                .email("test@naver.com")
                .level(1L)
                .encodedPassword("password")
                .role(UserRoleEnum.USER)
                .profileImage("")
                .build();

     userRepository.save(testUser);

     //Restaurant1 생성
        String restaurantName = "신전떡볶이";
        double latitude = 126.94830822613552;
        double longitude = 37.49054133559972;
        String address = "서울특별시 관악구 청룡길 38";
        String restaurantType = "포장마차";
        String fried = "true";
        String sundae = "순대만";
        String tteokbokkiType = "밀떡";
        String image = "testImage.png";
        Location location = new Location(address, latitude, longitude);

        this.restaurant1 = Restaurant.builder()
                .restaurantName(restaurantName)
                .restaurantType(restaurantType)
                .image(image)
                .sundae(sundae)
                .fried(fried)
                .user(testUser)
                .tteokbokkiType(tteokbokkiType)
                .location(location)
                .restaurantLikesCount(0)
                .build();

        this.restaurant2 = Restaurant.builder()
                .restaurantName("2번떡볶이집")
                .restaurantType(restaurantType)
                .image(image)
                .sundae(sundae)
                .fried(fried)
                .user(testUser)
                .tteokbokkiType(tteokbokkiType)
                .location(location)
                .restaurantLikesCount(0)
                .build();

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

    }


}