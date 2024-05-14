package org.sparta.testmemo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sparta.testmemo.dto.ScheduleRequestDto;


@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String password;
    private String date;

    public Schedule(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.assignee = requestDto.getAssignee();
        this.password = requestDto.getPassword();
        this.date = requestDto.getDate();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.assignee = requestDto.getAssignee();
        this.date = requestDto.getDate();
    }
}
/*
{
  "title": "수정된 제목",
  "description": "수정된 내용",
  "assignee": "수정된 담당자",
  "password": "수정된 비밀번호",
  "date": "수정된 작성일"
}
 */