package com.example.foodmap.repository;

import com.example.foodmap.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    Page <Meeting> findByOrderByModifiedAtDesc(Pageable pageable);


    void deleteAllByMeetingDateLessThan(LocalDateTime daytime);

    @Query("select distinct m from Meeting m left join fetch m.meetingComments where m.id = :id")
   Optional<Meeting> findById(@Param("id")Long id);

}
