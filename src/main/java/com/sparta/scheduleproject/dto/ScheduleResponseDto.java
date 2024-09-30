package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String username;
    private String todo;
    private String created_date;
    private String edited_date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUsername();
        this.todo = schedule.getTodo();
        this.created_date = schedule.getCreated_date();
        this.edited_date = schedule.getEdited_date();
    }

    public ScheduleResponseDto(Long id, String username, String todo, String created_date, String edited_date) {
        this.id = id;
        this.username = username;
        this.todo = todo;
        this.created_date = created_date;
        this.edited_date = edited_date;
    }
}
