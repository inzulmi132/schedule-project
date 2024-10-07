package com.sparta.scheduleproject.controller;

import com.sparta.scheduleproject.dto.ScheduleRequestDto;
import com.sparta.scheduleproject.dto.ScheduleResponseDto;
import com.sparta.scheduleproject.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;
    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO SCHEDULE (USERNAME, PASSWORD, TODO) VALUES (?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, requestDto.getUsername());
            ps.setString(2, requestDto.getPassword());
            ps.setString(3, requestDto.getTodo());
            return ps;
        }, keyHolder);
        Schedule schedule = findBy(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("")
    public List<ScheduleResponseDto> getAllSchedules(@RequestParam(required = false) String username, @RequestParam(required = false) String update_date) {
        String sql = "SELECT ID, USERNAME, TODO, CREATE_DATE, UPDATE_DATE FROM SCHEDULE";
        if(username != null && update_date != null) sql += " WHERE USERNAME = '" + username + "' AND UPDATE_DATE LIKE '" + update_date + "%'";
        else if(username != null) sql += " WHERE USERNAME = '" + username + "'";
        else if(update_date != null) sql += " WHERE UPDATE_DATE LIKE '" + update_date + "%'";
        sql += " ORDER BY UPDATE_DATE DESC";
        return getScheduleList(sql);
    }

    @GetMapping("/{id}")
    public ScheduleResponseDto getOneSchedule(@PathVariable Long id) {
        Schedule schedule = findBy(id);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("/{page}/{size}")
    public List<ScheduleResponseDto> getPageSchedules(@PathVariable int page, @PathVariable int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT ID, USERNAME, TODO, CREATE_DATE, UPDATE_DATE FROM SCHEDULE LIMIT " + size + " OFFSET " + offset;
        return getScheduleList(sql);
    }

    public List<ScheduleResponseDto> getScheduleList(String sql) {
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String username = rs.getString("username");
            String todo = rs.getString("todo");
            String create_date = rs.getString("create_date");
            String update_date = rs.getString("update_date");
            return new ScheduleResponseDto(id, username, todo, create_date, update_date);
        });
    }

    @PutMapping("")
    public Long updateSchedule(@RequestParam Long id, @RequestBody ScheduleRequestDto requestDto) {
        String password = requestDto.getPassword();
        Schedule schedule = findBy(id, password);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "UPDATE SCHEDULE SET USERNAME = ?, TODO = ?, UPDATE_DATE = CURRENT_TIMESTAMP WHERE ID = ? AND PASSWORD = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getTodo(), id, password);
        return id;
    }

    @DeleteMapping("")
    public Long deleteSchedule(@RequestParam Long id, @RequestParam String password) {
        Schedule schedule = findBy(id, password);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "DELETE FROM SCHEDULE WHERE ID = ? AND PASSWORD = ?";
        jdbcTemplate.update(sql, id, password);
        return id;
    }

    private Schedule findBy(Long id) {
        return findBy(id, null);
    }
    private Schedule findBy(Long id, String password) {
        String sql = "SELECT * FROM SCHEDULE WHERE ID = " + id;
        if(password != null) sql += " AND PASSWORD = " + password;
        return jdbcTemplate.query(sql, resultset -> {
            if(!resultset.next()) return null;
            Schedule schedule = new Schedule();
            schedule.setId(resultset.getLong("id"));
            schedule.setUsername(resultset.getString("username"));
            schedule.setPassword(resultset.getString("password"));
            schedule.setTodo(resultset.getString("todo"));
            schedule.setCreate_date(resultset.getString("create_date"));
            schedule.setUpdate_date(resultset.getString("update_date"));
            return schedule;
        });
    }
}
