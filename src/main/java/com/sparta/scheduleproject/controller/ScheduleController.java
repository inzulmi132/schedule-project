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

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedules")
    public ScheduleResponseDto crateSchedule(@RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO SCHEDULE (NAME, PASSWORD, TODO, CREATE, EDIT) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getName());
            ps.setString(2, schedule.getPassword());
            ps.setString(3, schedule.getTodo());
            ps.setString(4, schedule.getCreate());
            ps.setString(5, schedule.getEdit());
            return ps;
        }, keyHolder);
        schedule.setId(keyHolder.getKey().longValue());

        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules() {
        String sql = "SELECT * FROM SCHEDULE";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String todo = rs.getString("todo");
                String create = rs.getString("create");
                String edit = rs.getString("edit");
                return new ScheduleResponseDto(id, name, password, todo, create, edit);
        });
    }

    @PutMapping("/schedules")
    public Long updateSchedule(@RequestParam Long id, @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = findById(id);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "UPDATE SCHEDULE SET NAME = ?, TODO = ?, EDIT = ? WHERE ID = ?";
        jdbcTemplate.update(sql, requestDto.getName(), requestDto.getTodo(), requestDto.getDate(), id);
        return id;
    }

    @DeleteMapping("/schedules")
    public Long deleteSchedule(@RequestParam Long id) {
        Schedule schedule = findById(id);
        if(schedule == null) throw new RuntimeException("Schedule not found");

        String sql = "DELETE FROM SCHEDULE WHERE ID = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    private Schedule findById(Long id) {
        String sql = "SELECT * FROM SCHEDULE WHERE id = ?";
        return jdbcTemplate.query(sql, resultset -> {
            if(!resultset.next()) return null;
            Schedule schedule = new Schedule();
            schedule.setId(resultset.getLong("id"));
            schedule.setName(resultset.getString("name"));
            schedule.setPassword(resultset.getString("password"));
            schedule.setTodo(resultset.getString("todo"));
            schedule.setCreate(resultset.getString("create"));
            schedule.setEdit(resultset.getString("edit"));
            return schedule;
        });
    }
}
