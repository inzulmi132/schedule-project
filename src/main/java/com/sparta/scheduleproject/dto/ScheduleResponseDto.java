package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
