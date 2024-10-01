package com.sparta.scheduleproject.controller;

import com.sparta.scheduleproject.dto.UserRequestDto;
import com.sparta.scheduleproject.dto.UserResponseDto;
import com.sparta.scheduleproject.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        String sql = "INSERT INTO USER (USERNAME, PASSWORD, EMAIL) VALUES (?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, requestDto.getUsername());
            ps.setString(2, requestDto.getPassword());
            ps.setString(3, requestDto.getEmail());
            return ps;
        });
        User user = findByUnPw(requestDto.getUsername(), requestDto.getPassword());
        return new UserResponseDto(user);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        String sql = "SELECT USERNAME, EMAIL, CREATE_DATE, UPDATE_DATE FROM USER ORDER BY CREATE_DATE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String username = rs.getString("username");
            String email = rs.getString("email");
            String createDate = rs.getString("CREATE_DATE");
            String updateDate = rs.getString("UPDATE_DATE");
            return new UserResponseDto(username, email, createDate, updateDate);
        } );
    }

    @PutMapping("/users")
    public String updateUser(@RequestParam String username, @RequestParam String password, @RequestBody UserRequestDto requestDto) {
        User user = findByUnPw(username, password);
        if(user == null) throw new RuntimeException("User not found");

        String sql = "UPDATE USER SET USERNAME=?, PASSWORD=?, EMAIL=?, UPDATE_DATE = CURRENT_TIMESTAMP WHERE USERNAME=? AND PASSWORD=?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getPassword(), requestDto.getEmail(), username, password);
        return username;
    }

    @DeleteMapping("/users")
    public String deleteUser(@RequestParam String username, @RequestParam String password) {
        User user = findByUnPw(username, password);
        if(user == null) throw new RuntimeException("User not found");

        String sql = "DELETE FROM USER WHERE USERNAME=? AND PASSWORD=?";
        jdbcTemplate.update(sql, username, password);
        return username;
    }

    private User findByUnPw(String username, String password) {
        String sql = "SELECT * FROM USER WHERE USERNAME = ? AND PASSWORD = ?";
        return jdbcTemplate.query(sql, resultset -> {
            if(!resultset.next()) return null;
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(resultset.getString("EMAIL"));
            user.setCreate_date(resultset.getString("CREATE_DATE"));
            user.setUpdate_date(resultset.getString("UPDATE_DATE"));
            return user;
        }, username, password);
    }
}
