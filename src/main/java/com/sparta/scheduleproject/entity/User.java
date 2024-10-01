package com.sparta.scheduleproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String create_date;
    private String update_date;
}
