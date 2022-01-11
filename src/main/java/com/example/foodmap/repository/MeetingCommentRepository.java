package com.example.foodmap.repository;


import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingComment;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;


public interface MeetingCommentRepository extends JpaRepository<MeetingComment,Long> {
    List<MeetingComment> findAllByOrderByModifiedAtDesc();

    List<MeetingComment>findMeetingCommentByMeeting(Meeting meeting);


}
