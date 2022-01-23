package com.example.foodmap.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Meeting extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String restaurant;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String meetingTitle;

    @Column(nullable = false)
    private String content;


    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul" )
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul" )
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime meetingDate;

    @ColumnDefault(value = "0")
    private int viewCount;

    @Column
    private int limitPeople;

    @OneToMany(mappedBy = "meeting",cascade = CascadeType.ALL)
    List<MeetingParticipate> meetingParticipates = new ArrayList<>();

    @OneToMany(mappedBy = "meeting",cascade = CascadeType.ALL)
    List<MeetingComment>meetingComments = new ArrayList<>();

    @ColumnDefault(value = "0")
    private int nowPeople;

    @Builder
    public Meeting(User user, String restaurant, Long restaurantId, String meetingTitle, String content, String location, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime meetingDate, int viewCount, int limitPeople, int nowPeople) {
        this.user = user;
        this.restaurant = restaurant;
        this.restaurantId = restaurantId;
        this.meetingTitle = meetingTitle;
        this.content = content;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meetingDate = meetingDate;
        this.viewCount = viewCount;
        this.limitPeople = limitPeople;
        this.nowPeople = nowPeople;
    }


    public void addnowPeople() {
        this.nowPeople = nowPeople+1;
    }
    public void subnowPeople() {
        this.nowPeople = nowPeople-1;
    }
}
