package com.crack.domain.user.service;

import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;
import com.crack.domain.user.entity.Region;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.global.config.jwt.JwtToken;
import com.crack.global.config.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import java.beans.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Override
  @Transactional
  public void signUp(UserInfoRequestDto userInfoRequestDto) {
    userInfoRequestDto.encodePassword(passwordEncoder.encode(userInfoRequestDto.getPassword()));
    userRepository.save(userInfoRequestDto.toSaveUser());
  }

  @Override
  @Transactional
  public TokenResponseDto login(LoginRequestDto loginRequestDto) {
    User user = userRepository.findUserByEmail(loginRequestDto.getEmail()).orElseThrow(()->  new RuntimeException("no user"));
    JwtToken jwtToken = jwtUtil.generateToken(user.getEmail());
    return new TokenResponseDto(jwtToken);
  }
  @Override
  @Transactional
  public UserInfoResponseDto info(Long userId){
    User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("no user"));
    return UserInfoResponseDto.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .phoneNumber(user.getPhoneNumber())
        .region(user.getRegion())
        .build();
  }
}
