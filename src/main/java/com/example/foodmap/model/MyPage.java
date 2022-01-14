package com.example.foodmap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyPage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Restaurant> restaurantList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Review> reviewList = new ArrayList<>();
}
