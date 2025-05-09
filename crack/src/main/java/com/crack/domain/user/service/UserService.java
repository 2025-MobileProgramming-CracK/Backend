package com.crack.domain.user.service;

import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;

public interface UserService {

  void signUp(UserInfoRequestDto userInfoRequestDto);

  TokenResponseDto login(LoginRequestDto loginRequestDto);

  UserInfoResponseDto info(Long userId);


}
