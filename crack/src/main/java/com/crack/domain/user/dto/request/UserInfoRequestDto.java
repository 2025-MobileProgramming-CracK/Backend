package com.crack.domain.user.dto.request;

import com.crack.domain.user.entity.Region;
import com.crack.domain.user.entity.Role;
import com.crack.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class UserInfoRequestDto {

  private String username;
  private String email;
  private String password;
  private String phoneNumber;
  private Region region;

  public User toSaveUser(){
    return User.builder()
        .username(this.username)
        .email(this.email)
        .password(this.password)
        .phoneNumber(this.phoneNumber)
        .region(this.region)
        .role(Role.ROLE_USER)
        .build();
  }
  public void encodePassword(String encodingPassword) {
    this.password = encodingPassword;
  }

}