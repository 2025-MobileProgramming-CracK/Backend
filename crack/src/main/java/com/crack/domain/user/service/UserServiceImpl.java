package com.crack.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
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
  private final AmazonS3 s3Client;
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
  public void addProfile(Long userId, MultipartFile imageFile) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }
      String fileName = "profile/" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();

      try {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());
        metadata.setContentType(imageFile.getContentType());
        s3Client.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), metadata));
        String fileUrl = s3Client.getUrl(bucket, fileName).toString();

        User user = userRepository.findById(userId).get();
        user.setImageUrl(fileUrl);
        userRepository.save(user);

      } catch (IOException e) {
        throw new RuntimeException("이미지 업로드 실패", e);
      }
  }


}
