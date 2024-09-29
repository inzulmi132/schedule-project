package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String username;
    private String todo;
    private String created;
    private String edited;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUsername();
        this.todo = schedule.getTodo();
        this.created = schedule.getCreated();
        this.edited = schedule.getEdited();
    }

    public ScheduleResponseDto(Long id, String username, String todo, String created, String edited) {
        this.id = id;
        this.username = username;
        this.todo = todo;
        this.created = created;
        this.edited = edited;
    }
}
