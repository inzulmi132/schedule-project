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
        String sql = "INSERT INTO SCHEDULE (USERNAME, PASSWORD, TODO, CREATED, EDIT) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getUsername());
            ps.setString(2, schedule.getPassword());
            ps.setString(3, schedule.getTodo());
            ps.setString(4, schedule.getCreated());
            ps.setString(5, schedule.getEdited());
            return ps;
        }, keyHolder);
        schedule.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules(@RequestParam(required = false) String username, @RequestParam(required = false) String edit) {
        String sql = "SELECT ID, USERNAME, TODO, CREATED, EDIT FROM SCHEDULE";
        if(username != null && edit != null) sql += " WHERE USERNAME = " + username + " AND EDIT = " + edit;
        else if(username != null) sql += " WHERE USERNAME = " + username;
        else if(edit != null) sql += " WHERE EDIT = " + edit;
        sql += " ORDER BY EDIT DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String username0 = rs.getString("username");
                String todo = rs.getString("todo");
                String created = rs.getString("created");
                String edit0 = rs.getString("edited");
                return new ScheduleResponseDto(id, username0, todo, created, edit0);
        });
    }

    @GetMapping("/schedules/one")
    public ScheduleResponseDto getOneSchedule(@RequestParam Long id) {
        Schedule schedule = findById(id);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        return new ScheduleResponseDto(schedule);
    }

    @PutMapping("/schedules")
    public Long updateSchedule(@RequestParam Long id, @RequestParam String password, @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = findByIdPw(id, password);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "UPDATE SCHEDULE SET USERNAME = ?, TODO = ?, EDIT = ? WHERE ID = ? AND PASSWORD = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getTodo(), requestDto.getDate(), id, password);
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
            schedule.setCreated(resultset.getString("created"));
            schedule.setEdited(resultset.getString("edited"));
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
            schedule.setCreated(resultset.getString("created"));
            schedule.setEdited(resultset.getString("edited"));
            return schedule;
        }, id, password);
    }
}
