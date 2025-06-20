package com.crack.domain.calendar.service;

import com.crack.domain.calendar.dto.request.CreateCalendarDto;
import com.crack.domain.calendar.dto.response.GetDateResponseDto;
import com.crack.domain.calendar.dto.response.GetResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface CalendarService {


  Long create(CreateCalendarDto createCalendarDto, Long userId);

  List<GetDateResponseDto> getCalendar(Long userId, LocalDate day);

  void deleteCalendar(Long userId, Long dateId);
  List<LocalDate> getMonthCalendar(Long userId, Long year, Long month);
}
