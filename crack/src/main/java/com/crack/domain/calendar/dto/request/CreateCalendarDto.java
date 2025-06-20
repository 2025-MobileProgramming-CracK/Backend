package com.crack.domain.calendar.dto.request;


import java.time.LocalDateTime;
import lombok.Getter;


@Getter
public class CreateCalendarDto{
  private String title;
  private String description;
  private LocalDateTime date;
}
