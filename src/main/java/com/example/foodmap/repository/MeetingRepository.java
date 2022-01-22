package com.example.foodmap.repository;

import com.example.foodmap.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    Page <Meeting> findByOrderByModifiedAtDesc(Pageable pageable);


    void deleteAllByMeetingDateLessThan(LocalDateTime daytime);

}
