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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO SCHEDULE (USERNAME, PASSWORD, TODO, CREATED_DATE, UPDATED_DATE) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getUsername());
            ps.setString(2, schedule.getPassword());
            ps.setString(3, schedule.getTodo());
            ps.setString(4, schedule.getCreated_date());
            ps.setString(5, schedule.getUpdated_date());
            return ps;
        }, keyHolder);
        schedule.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules(@RequestParam(required = false) String username, @RequestParam(required = false) String updated_date) {
        String sql = "SELECT ID, USERNAME, TODO, CREATED_DATE, UPDATED_DATE FROM SCHEDULE";
        if(username != null && updated_date != null) sql += " WHERE USERNAME = " + username + " AND UPDATED_DATE LIKE " + updated_date + "%";
        else if(username != null) sql += " WHERE USERNAME = " + username;
        else if(updated_date != null) sql += " WHERE UPDATED_DATE LIKE '" + updated_date + "%'";
        sql += " ORDER BY UPDATED_DATE DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String username0 = rs.getString("username");
                String todo = rs.getString("todo");
                String created_date = rs.getString("created_date");
                String updated_date0 = rs.getString("updated_date");
                return new ScheduleResponseDto(id, username0, todo, created_date, updated_date0);
        });
    }

    @GetMapping("/schedules/{id}")
    public ScheduleResponseDto getOneSchedule(@PathVariable Long id) {
        Schedule schedule = findById(id);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        return new ScheduleResponseDto(schedule);
    }

    @PutMapping("/schedules")
    public Long updateSchedule(@RequestParam Long id, @RequestBody ScheduleRequestDto requestDto) {
        String password = requestDto.getPassword();
        Schedule schedule = findByIdPw(id, password);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "UPDATE SCHEDULE SET USERNAME = ?, TODO = ?, UPDATED_DATE = ? WHERE ID = ? AND PASSWORD = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getTodo(), requestDto.getDatetime(), id, password);
        return id;
    }

    @DeleteMapping("/schedules")
    public Long deleteSchedule(@RequestParam Long id, @RequestParam String password) {
        Schedule schedule = findByIdPw(id, password);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "DELETE FROM SCHEDULE WHERE ID = ? AND PASSWORD = ?";
        jdbcTemplate.update(sql, id, password);
        return id;
    }

    private Schedule findById(Long id) {
        String sql = "SELECT * FROM SCHEDULE WHERE ID = ?";
        return jdbcTemplate.query(sql, resultset -> {
            if(!resultset.next()) return null;
            Schedule schedule = new Schedule();
            schedule.setId(resultset.getLong("id"));
            schedule.setUsername(resultset.getString("username"));
            schedule.setPassword(resultset.getString("password"));
            schedule.setTodo(resultset.getString("todo"));
            schedule.setCreated_date(resultset.getString("created_date"));
            schedule.setUpdated_date(resultset.getString("updated_date"));
            return schedule;
        }, id);
    }

    private Schedule findByIdPw(Long id, String password) {
        String sql = "SELECT * FROM SCHEDULE WHERE ID = ? AND PASSWORD = ?";
        return jdbcTemplate.query(sql, resultset -> {
            if(!resultset.next()) return null;
            Schedule schedule = new Schedule();
            schedule.setId(resultset.getLong("id"));
            schedule.setUsername(resultset.getString("username"));
            schedule.setPassword(resultset.getString("password"));
            schedule.setTodo(resultset.getString("todo"));
            schedule.setCreated_date(resultset.getString("created_date"));
            schedule.setUpdated_date(resultset.getString("updated_date"));
            return schedule;
        }, id, password);
    }
}
