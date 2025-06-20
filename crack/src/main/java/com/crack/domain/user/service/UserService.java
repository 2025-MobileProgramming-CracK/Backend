package com.crack.domain.user.service;

import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  void signUp(UserInfoRequestDto userInfoRequestDto);

  TokenResponseDto login(LoginRequestDto loginRequestDto);

  UserInfoResponseDto info(Long userId);

  void deleteUser(Long userId);

  String addProfile(Long userId, MultipartFile imageFile);

}
