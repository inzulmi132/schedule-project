package com.sparta.scheduleproject.dto;

import com.sparta.scheduleproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String email;
    private String create_date;
    private String update_date;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.create_date = user.getCreate_date();
        this.update_date = user.getUpdate_date();
    }

    public UserResponseDto(String username, String email, String create_date, String update_date) {
        this.username = username;
        this.email = email;
        this.create_date = create_date;
        this.update_date = update_date;
    }
}
