package com.example.foodmap.service;

import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.RestaurantLikes;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.RestaurantLikesRepository;
import com.example.foodmap.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RestaurantLikesService {

    private final RestaurantLikesRepository restaurantLikesRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void clickReviewLikes(User user, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(null);
        RestaurantLikes existLike = restaurantLikesRepository.findByUserAndRestaurant(user, restaurant);
        if(restaurantLikesRepository.existsByUserAndRestaurant(user,restaurant)) {
            restaurantLikesRepository.deleteById(existLike.getId());
            restaurantRepository.downLikeCnt(restaurantId);
        }
        else{
            RestaurantLikes restaurantLikes = RestaurantLikes.builder()
                    .user(user)
                    .restaurant(restaurant)
                    .build();
            restaurantLikes.addRestaurant(restaurant);
            restaurantLikesRepository.save(restaurantLikes);
            restaurantRepository.upLikeCnt(restaurantId);
        }
        restaurantRepository.save(restaurant);
    }

}
