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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "게시글 API")
@RequestMapping("/post")
@RestController
@AllArgsConstructor
@Slf4j
public class PostController {
  private final PostService postService;

  @Operation(summary = "게시글 생성", description = "새로운 게시글 생성합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomApiResponse<String>> create(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @ModelAttribute PostCreateRequestDto postCreateRequestDto
  ) {
    Long userId = customUserDetails.getId();
    log.info("게시글 생성 요청:image={}", postCreateRequestDto.getImage());
    String resultMessage = postService.create(userId, postCreateRequestDto, postCreateRequestDto.getImage());

    HttpStatus status = resultMessage.contains("생성 완료") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(CustomApiResponse.onSuccess(resultMessage));
  }


  @Operation(summary = "모든 게시글 최신순 조회", description = "모든 게시글을 최신순으로 조회합니다.")
  @GetMapping("/all")
  public ResponseEntity<CustomApiResponse<List<PostGetResponseDto>>> getAllPosts() {
    List<PostGetResponseDto> posts = postService.getAllPosts();
    return ResponseEntity.ok(CustomApiResponse.onSuccess(posts));
  }
  @Operation(summary = "게시글에 하트", description = "게시글에 좋아요를 합니다.(좋아요 무한대 가능)")
  @PostMapping("/like")
  public ResponseEntity<CustomApiResponse<String>> likePost(@RequestParam Long postId) {
    postService.likePost(postId);
    return ResponseEntity.ok(CustomApiResponse.onSuccess("좋아요가 등록되었습니다."));
  }
  @Operation(summary = "모든 게시글 좋아요순 조회", description = "모든 게시글을 좋아요순으로 조회합니다.")
  @GetMapping("/all/like")
  public ResponseEntity<CustomApiResponse<List<PostGetResponseDto>>> getLikePosts() {
    List<PostGetResponseDto> posts = postService.getLikePosts();
    return ResponseEntity.ok(CustomApiResponse.onSuccess(posts));
  }
  @Operation(summary = "내가 올린 게시글 조회", description = "내가 올린 게시글을 조회합니다.")
  @GetMapping("/myPosts")
  public ResponseEntity<CustomApiResponse<List<PostGetResponseDto>>> getMyPosts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getId();
    List<PostGetResponseDto> myPosts = postService.getMyPosts(userId);
    return ResponseEntity.ok(CustomApiResponse.onSuccess(myPosts));
  }



}
