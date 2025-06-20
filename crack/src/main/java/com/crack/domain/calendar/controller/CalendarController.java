package com.crack.domain.calendar.controller;


import com.crack.domain.calendar.dto.request.CreateCalendarDto;
import com.crack.domain.calendar.dto.request.GetRequestDto;
import com.crack.domain.calendar.dto.response.GetDateResponseDto;
import com.crack.domain.calendar.service.CalendarService;
import com.crack.global.common.CustomApiResponse;
import com.crack.global.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  @PostMapping()
  public ResponseEntity<CustomApiResponse<List<GetDateResponseDto>>> getCalendar(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody
      GetRequestDto getRequestDto) {
    Long userId = customUserDetails.getId();
    LocalDate day = getRequestDto.getDay();
    List <GetDateResponseDto> getDateResponseDto = calendarService.getCalendar(userId, day);
    return ResponseEntity.ok(CustomApiResponse.onSuccess(getDateResponseDto));
  }
}
