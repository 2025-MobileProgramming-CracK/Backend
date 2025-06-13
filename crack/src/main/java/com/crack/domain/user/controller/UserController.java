package com.crack.domain.user.controller;


import com.crack.domain.user.dto.request.LoginRequestDto;
import com.crack.domain.user.dto.request.UserInfoRequestDto;
import com.crack.domain.user.dto.response.TokenResponseDto;
import com.crack.domain.user.dto.response.UserInfoResponseDto;
import com.crack.domain.user.service.UserService;
import com.crack.global.common.CustomApiResponse;
import com.crack.global.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "회원관리 API")
@RequestMapping("/user")
@RestController
@AllArgsConstructor
@Slf4j

public class UserController {
  private final UserService userService;

  @Operation(summary = "회원가입", description = "서비스를 이용할 새로운 회원을 등록합니다,")
  @PostMapping("/signUp")
  public ResponseEntity<CustomApiResponse<String>> signUp(@RequestBody UserInfoRequestDto userInfoRequestDto) {
    userService.signUp(userInfoRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess("회원가입성공함"));
  }

  @Operation(summary = "로그인", description = "회원 로그인을 처리합니다.")
  @PostMapping("/logIn")
  public ResponseEntity<CustomApiResponse<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
    TokenResponseDto tokenResponseDto = userService.login(loginRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess(tokenResponseDto));
  }
  @Operation(summary = "사용자 정보 조회", description = "사용자의 정보를 조회합니다.")
  @GetMapping("/info")
  public ResponseEntity<CustomApiResponse<UserInfoResponseDto>> info(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getId();
    UserInfoResponseDto userInfoResponseDto = userService.info(userId);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess(userInfoResponseDto));
  }


  @Operation(summary = "사용자 탈퇴", description = "사용자의 탈퇴합니다.")
  @PostMapping("/delete")
  public ResponseEntity<CustomApiResponse<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getId();
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess("탈퇴 성공함"));
  }

  @Operation(summary = "사용자 프로필 생성", description = "사용자의 정보를 조회합니다.")
  @PostMapping(value = "/image", consumes =MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomApiResponse<String>> addProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestPart MultipartFile imageFile) {
    Long userId = customUserDetails.getId();
    userService.addProfile(userId,imageFile);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess("프로필 생성됌"));
  }









}
