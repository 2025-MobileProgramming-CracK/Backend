package com.crack.domain.user.service;

import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.global.config.aws.S3Service;
import com.crack.global.config.jwt.JwtToken;
import com.crack.global.config.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final S3Service s3Service;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

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
    User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
    return UserInfoResponseDto.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .phoneNumber(user.getPhoneNumber())
        .region(user.getRegion())
        .profileImageUrl(user.getImageUrl())
        .build();
  }
  @Override
  @Transactional
  public void deleteUser(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }
    userRepository.deleteById(userId);
  }

  @Override
  @Transactional
  public String addProfile(Long userId, MultipartFile imageFile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    String imageUrl = s3Service.uploadFile("profile", imageFile);
    user.setImageUrl(imageUrl);
    userRepository.save(user);
    return imageUrl;
  }

}
