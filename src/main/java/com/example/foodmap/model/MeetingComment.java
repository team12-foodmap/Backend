package com.example.foodmap.model;

import com.example.foodmap.dto.meeting.MeetingUpdateRequestDto;
import com.example.foodmap.validator.MeetingValidator;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
public class MeetingComment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //DDL 상에서 연관된 필드 연쇄 삭제
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Meeting meeting;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MeetingComment parent;

    @OneToMany(mappedBy = "parent",orphanRemoval = true ,cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MeetingComment> children = new ArrayList<>();

    @Builder
    public MeetingComment(String content, User user,Meeting meeting ,MeetingComment parent) {

        MeetingValidator.isCommentValidator(meeting,content);
        this.user=user;
        this.meeting=meeting;
        this.content=content;
        this.parent=parent;

    }

    public void updateMeeting (MeetingUpdateRequestDto meetingUpdateRequestDto) {
        this.content=meetingUpdateRequestDto.getContent();
    }

    public void addMeeting(Meeting meeting){
        this.meeting=meeting;
        meeting.meetingComments.add(this);
    }

}
