package com.crack.domain.post.controller;


import com.crack.domain.post.dto.request.PostCreateRequestDto;
import com.crack.domain.post.dto.response.PostGetResponseDto;
import com.crack.domain.post.service.PostService;
import com.crack.global.common.CustomApiResponse;
import com.crack.global.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Post", description = "게시글 API")
@RequestMapping("/post")
@RestController
@AllArgsConstructor
@Slf4j
public class PostController {
  private final PostService postService;

  @Operation(summary = "게시글 생성", description = "새로운 게시글 생성합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomApiResponse<Long>> create(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody PostCreateRequestDto postCreateRequestDto,@RequestPart("image")  MultipartFile image) {
    Long userId = customUserDetails.getId();
    Long postId=postService.create(userId, postCreateRequestDto, image);
    return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.onSuccess(postId));
  }
  @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 최신순으로 조회합니다.")
  @GetMapping("/all")
  public ResponseEntity<CustomApiResponse<List<PostGetResponseDto>>> getAllPosts() {
    List<PostGetResponseDto> posts = postService.getAllPosts();
    return ResponseEntity.ok(CustomApiResponse.onSuccess(posts));
  }


}
