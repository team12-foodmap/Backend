package com.example.foodmap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;




@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Restaurant extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String restaurantName;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private String restaurantType;

    @Column
    private String fried;

    @Column(nullable = false)
    private String sundae;

    @Column(nullable = false)
    private String tteokbokkiType;

    private String image;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    List<RestaurantLikes> restaurantLikes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    List<Review> reviews = new ArrayList<>();

    @ColumnDefault(value="0")
    private int restaurantLikesCount;

}
