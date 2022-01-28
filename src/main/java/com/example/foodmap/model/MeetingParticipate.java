package com.example.foodmap.model;

import com.example.foodmap.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MeetingParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Meeting meeting;

    @Builder
    public MeetingParticipate(Meeting meeting, UserDetailsImpl userDetails) {
        this.meeting=meeting;
        this.user=userDetails.getUser();
    }



    public void addMeeting(Meeting meeting){
        this.meeting=meeting;
        meeting.meetingParticipates.add(this);
    }
}
