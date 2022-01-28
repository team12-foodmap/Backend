package com.example.foodmap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
@Builder
public class RestaurantLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    public RestaurantLikes(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public void addRestaurant(Restaurant restaurant){
        this.restaurant=restaurant;
        restaurant.getRestaurantLikes().add(this);
    }

}
