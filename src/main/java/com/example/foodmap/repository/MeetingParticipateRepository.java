package com.example.foodmap.repository;

import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingParticipate;
import com.example.foodmap.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingParticipateRepository extends JpaRepository<MeetingParticipate,Long> {
    Optional<MeetingParticipate> findByMeetingAndUser(Meeting meeting,User user);

    List<MeetingParticipate> findAllByUser(User user, Pageable pageable);

    List<MeetingParticipate> findAllByMeetingId(Long meetingId);
}
