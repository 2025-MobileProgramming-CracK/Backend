package com.crack.domain.calendar.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class GetDateResponseDto {
    private String title;
    private String description;
    private LocalDateTime date;

}
