package com.example.foodmap.controller;

import com.example.foodmap.dto.Restaurant.RankingResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantDetailResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.User;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.RestaurantService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    @ApiOperation("식당 등록")
    @PostMapping("/restaurants")
    public ResponseEntity<Long> saveRestaurant(@ModelAttribute RestaurantSaveRequestDto requestDto,
                                                 @RequestParam(name = "image",required = false) MultipartFile image,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails)   {

        log.info("식당 등록 들어오나요 여기는 controller");
        User user = userDetails.getUser();

        return ResponseEntity.ok().body(restaurantService.saveRestaurant(requestDto, user,image));
    }

    //내 근처 식당
    @ApiOperation("내 근처 식당 리스트 조회")
    @GetMapping("/restaurants")
    public List<RestaurantResponseDto> getRestaurants(
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        Location userLocation = userDetails.getUser().getLocation();

        double userLat = userLocation.getLatitude();
        double userLon = userLocation.getLongitude();

        return restaurantService.getRestaurants(userLat, userLon, page, size);
    }

    @ApiOperation("식당 상세페이지 조회")
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponseDto> getInfo(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok().body(restaurantService.getRestaurantDetail(restaurantId, userDetails.getUser()));
    }

//식당 찜하기 TOP3(거리별)
    @GetMapping("/restaurants/ranking")
    public ResponseEntity<List<RankingResponseDto>> getTop3ByRestaurant(@AuthenticationPrincipal Restaurant restaurant,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        List<RankingResponseDto> myLikeResponseDto = restaurantService.getTop3ByRestaurant(restaurant, user);
        return ResponseEntity.ok().body(myLikeResponseDto);
    }

    //로그인하지 않은 사용자(둘러보기) - 서울역 근처 식당 조회
    @GetMapping("/home")
    public List<RestaurantResponseDto> getRestaurantsInHome(@RequestParam int page,@RequestParam int size) {
        double lat = 126.97260868381068;
        double lon = 37.559187621837744;
        return restaurantService.getRestaurants(lat, lon, page, size);

    }

    //변화하는 위치별 식당조회
    @GetMapping("/restaurants/search")
    public List<RestaurantResponseDto> getRestaurantsInHome(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam double lat,
            @RequestParam double lon) {
        return restaurantService.getRestaurants(lat, lon, page, size);
    }
}
