package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String create;
    private String edit;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.todo = schedule.getTodo();
        this.create = schedule.getCreate();
        this.edit = schedule.getEdit();
    }

    public ScheduleResponseDto(Long id, String name, String password, String todo, String create, String edit) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.todo = todo;
        this.create = create;
        this.edit = edit;
    }
}
