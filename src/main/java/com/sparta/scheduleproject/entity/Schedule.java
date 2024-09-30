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
    private String username;
    private String password;
    private String todo;
    private String created_date;
    private String updated_date;

    public Schedule(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.created_date = requestDto.getDatetime();
        this.updated_date = requestDto.getDatetime();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.updated_date = requestDto.getDatetime();
    }
}
