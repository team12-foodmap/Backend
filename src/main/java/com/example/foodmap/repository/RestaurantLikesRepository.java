package com.example.foodmap.repository;

import com.example.foodmap.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantLikesRepository extends JpaRepository<RestaurantLikes, Long> {

    Boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
    List<RestaurantLikes> findAllByUser(User user, Pageable pageable);
    RestaurantLikes findByUserAndRestaurant(User user, Restaurant restaurant);

}