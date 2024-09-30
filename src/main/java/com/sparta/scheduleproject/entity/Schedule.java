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
    private String create_date;
    private String update_date;

    public Schedule(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.create_date = requestDto.getDatetime();
        this.update_date = requestDto.getDatetime();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.update_date = requestDto.getDatetime();
    }
}
