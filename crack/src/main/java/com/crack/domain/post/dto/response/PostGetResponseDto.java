package com.crack.domain.post.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostGetResponseDto {
  private Long id;
  private String title;
  private String content;
  private LocalDateTime date;

}
