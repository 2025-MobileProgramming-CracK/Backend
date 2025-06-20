package com.crack.domain.post.dto.response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostGetResponseDto {
  private Long id;
  private String userName;
  private String title;
  private String imageUrl;
  private Long likeCount;
  private LocalDateTime updatedAt;

}
