package org.sparta.testmemo.dto;

import lombok.Getter;
import org.sparta.testmemo.entity.Schedule;

@Getter

public class ScheduleResponseDto {

    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String password;
    private String date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.assignee = schedule.getAssignee();
        this.password = schedule.getPassword();
        this.date = schedule.getDate();
    }
}
