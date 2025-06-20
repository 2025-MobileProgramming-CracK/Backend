package com.crack.domain.user.dto.response;


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
  private String region;
  private String profileImageUrl;
}
