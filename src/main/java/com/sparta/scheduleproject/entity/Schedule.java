package com.sparta.scheduleproject.entity;

import com.sparta.scheduleproject.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String create;
    private String edit;

    public Schedule(ScheduleRequestDto requestDto) {
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.create = requestDto.getDate();
        this.edit = requestDto.getDate();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.edit = requestDto.getDate();
    }
}
