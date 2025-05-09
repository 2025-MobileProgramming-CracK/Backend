package com.crack.domain.user.dto.response;

import com.crack.domain.user.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter


public class UserInfoResponseDto {
  private String email;
  private String username;
  private String phoneNumber;
  private Region region;
}
