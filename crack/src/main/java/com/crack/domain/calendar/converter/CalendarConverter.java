package com.crack.domain.calendar.converter;

import com.crack.domain.calendar.entity.Calendar;
import com.crack.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

public class CalendarConverter {

  public static Calendar toEntity(String title, String description, LocalDateTime date, User user) {
    return Calendar.builder()
        .title(title)
        .description(description)
        .date(date)
        .user(user)
        .build();
  }
}
