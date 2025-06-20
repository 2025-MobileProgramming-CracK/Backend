package com.crack.domain.calendar.controller;


import com.crack.domain.calendar.dto.request.CreateCalendarDto;

import com.crack.domain.calendar.dto.response.GetDateResponseDto;
import com.crack.domain.calendar.dto.response.GetResponseDto;
import com.crack.domain.calendar.service.CalendarService;
import com.crack.global.common.CustomApiResponse;
import com.crack.global.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Tag(name = "Calendar", description = "캘린더 API")
@RequestMapping("/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {
  private final CalendarService calendarService;


  @Operation(summary = "사용자 새로운 일정생성", description = "새로운 일정을 생성합니다.")
  @PostMapping("/create")
  public ResponseEntity<CustomApiResponse<Long>> create(@RequestBody CreateCalendarDto createCalendarDto , @AuthenticationPrincipal
      CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getId();
    Long calendarId = calendarService.create(createCalendarDto, userId);
    return ResponseEntity.ok(CustomApiResponse.onSuccess(calendarId));
  }
  @Operation(summary = "사용자 해당하는 일정 조회", description = "사용자의 해당 하는 일정을 조회합니다.")
  @GetMapping()
  public ResponseEntity<CustomApiResponse<List<GetDateResponseDto>>> getCalendar(@AuthenticationPrincipal CustomUserDetails customUserDetails,   @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
    Long userId = customUserDetails.getId();
    List <GetDateResponseDto> getDateResponseDto = calendarService.getCalendar(userId, day);
    return ResponseEntity.ok(CustomApiResponse.onSuccess(getDateResponseDto));
  }
  @Operation(summary = "사용자 일정 삭제", description = "사용자의 일정을 삭제합니다.")
  @DeleteMapping("/{date-id}")
  public ResponseEntity<CustomApiResponse<String>> deleteCalendar(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable ("date-id") Long dateId) {
    Long userId = customUserDetails.getId();
    calendarService.deleteCalendar(userId, dateId);
    return ResponseEntity.ok(CustomApiResponse.onSuccess("일정이 삭제되었습니다."));
  }
  @Operation(summary = "사용자 한달 일정 조회", description = "사용자의 한달 일정을 조회합니다.")
  @GetMapping("/month")
  public ResponseEntity<CustomApiResponse<List<LocalDate>>> getMonthCalendar(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestParam Long year, @RequestParam Long month) {
    Long userId = customUserDetails.getId();
    List<LocalDate> getMonthCalendar = calendarService.getMonthCalendar(userId, year,month);
    return ResponseEntity.ok(CustomApiResponse.onSuccess(getMonthCalendar));
  }


}
