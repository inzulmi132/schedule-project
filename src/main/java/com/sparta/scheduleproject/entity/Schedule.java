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
    private String created;
    private String edited;

    public Schedule(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.created = requestDto.getDate();
        this.edited = requestDto.getDate();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.edited = requestDto.getDate();
    }
}
