package com.example.foodmap.repository;

import com.example.foodmap.model.*;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantLikesRepository extends JpaRepository<RestaurantLikes, Long> {

    Boolean existsByUserAndRestaurant(User user, Restaurant restaurant);

    @Query("select r from RestaurantLikes r join fetch r.restaurant where r.user = :user")
    List<RestaurantLikes> findAllByUser(User user, Pageable pageable);

    RestaurantLikes findByUserAndRestaurant(User user, Restaurant restaurant);

}