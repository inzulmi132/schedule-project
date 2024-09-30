package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String username;
    private String todo;
    private String create_date;
    private String update_date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUsername();
        this.todo = schedule.getTodo();
        this.create_date = schedule.getCreate_date();
        this.update_date = schedule.getUpdate_date();
    }

    public ScheduleResponseDto(Long id, String username, String todo, String create_date, String update_date) {
        this.id = id;
        this.username = username;
        this.todo = todo;
        this.create_date = create_date;
        this.update_date = update_date;
    }
}
