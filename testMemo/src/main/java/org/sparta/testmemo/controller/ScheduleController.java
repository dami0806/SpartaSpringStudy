package org.sparta.testmemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.testmemo.dto.ScheduleRequestDto;
import org.sparta.testmemo.dto.ScheduleResponseDto;
import org.sparta.testmemo.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final Map<Long, Schedule> scheduleList = new ConcurrentHashMap<>();

    /**
     * 스케줄 post
     */
    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        try {
            log.info("Received schedule: {}", requestDto);

            Schedule schedule = new Schedule(requestDto);
            Long maxId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;
            schedule.setId(maxId);
            scheduleList.put(schedule.getId(), schedule);
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getScheduleList() {
        //List
        List<ScheduleResponseDto> scheduleResponseDtoList = scheduleList.values()
                .stream()
                .map(schedule ->
                        new ScheduleResponseDto(schedule)).toList();
        return scheduleResponseDtoList;
    }

    @PutMapping("/schedule/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        if (scheduleList.containsKey(id)) {
            Schedule schedule = scheduleList.get(id);

            //schedule 수정
            schedule.update(requestDto);

            schedule.setTitle(requestDto.getTitle());
            schedule.setDescription(requestDto.getDescription());
            schedule.setAssignee(requestDto.getAssignee());
            schedule.setPassword(requestDto.getPassword());
            schedule.setDate(requestDto.getDate());
            scheduleList.put(id, schedule);
            return schedule.getId();

        } else {
            throw new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedule/{id}")
    public Long deleteSchedule(@PathVariable Long id) {
        if (scheduleList.containsKey(id)) {
            scheduleList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다.");
        }
    }

    @PostMapping("/schedule/validatePassword/{id}")
    public ResponseEntity<Boolean> verifyPassword(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            String inputPassword = requestBody.get("password");

            Schedule schedule = scheduleList.get(id);
            if (schedule != null && schedule.getPassword().equals(inputPassword)) {
                return ResponseEntity.ok(true); // 비밀번호 일치 시 true 반환
            }
            return ResponseEntity.ok(false);
        }catch (Exception e) {
            log.error("Error verifying password: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
