package com.crack.domain.calendar.service;

import com.crack.domain.calendar.converter.CalendarConverter;
import com.crack.domain.calendar.dto.request.CreateCalendarDto;
import com.crack.domain.calendar.dto.response.GetDateResponseDto;
import com.crack.domain.calendar.dto.response.GetResponseDto;
import com.crack.domain.calendar.entity.Calendar;
import com.crack.domain.calendar.repository.CalendarRepository;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

  private final UserRepository userRepository;
  private final CalendarRepository calendarRepository;


  @Override
  public Long create(CreateCalendarDto createCalendarDto, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));
    Calendar calendar = CalendarConverter.toEntity(
        createCalendarDto.getTitle(),
        createCalendarDto.getDescription(),
        createCalendarDto.getDate(),
        user
    );
    return calendarRepository.save(calendar).getId();
  }

  @Override
  public List<GetDateResponseDto> getCalendar(Long userId, LocalDate day) {
    userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));


    LocalDateTime startOfDay = day.atStartOfDay(); // 2025-06-20T00:00:00
    LocalDateTime endOfDay = day.atTime(LocalTime.MAX); // 2025-06-20T23:59:59.999999999

    List<Calendar> calendars = calendarRepository.findAllByUserIdAndDateBetween(userId, startOfDay, endOfDay);

    return calendars.stream()
        .map(calendar -> GetDateResponseDto.builder()
            .title(calendar.getTitle())
            .description(calendar.getDescription())
            .date(calendar.getDate())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void deleteCalendar(Long userId, Long dateId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));
    Calendar calendar = calendarRepository.findById(dateId)
        .orElseThrow(() -> new IllegalArgumentException("없는 일정: " + dateId));

    calendarRepository.delete(calendar);
  }


  public List<LocalDate> getMonthCalendar(Long userId, Long year, Long month) {
    userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));

    LocalDate startOfMonth = LocalDate.of(year.intValue(), month.intValue(), 1);
    LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

    // 날짜 범위에 해당하는 일정 모두 가져오기
    List<Calendar> calendars = calendarRepository.findAllByUserIdAndDateBetween(
        userId,
        startOfMonth.atStartOfDay(),
        endOfMonth.atTime(23, 59, 59)
    );

    // 일정 있는 날짜만 추출해서 중복 제거 후 반환
    return calendars.stream()
        .map(calendar -> calendar.getDate().toLocalDate())  // LocalDateTime → LocalDate
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }


}
