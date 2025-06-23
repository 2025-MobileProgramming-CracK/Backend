package com.crack.domain.post.service;


import com.crack.domain.post.converter.PostConverter;
import com.crack.domain.post.dto.request.PostCreateRequestDto;
import com.crack.domain.post.dto.response.PostGetResponseDto;
import com.crack.domain.post.entity.Post;
import com.crack.domain.post.repository.PostRepository;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.global.config.aws.S3Service;
import com.crack.global.config.fastapi.ExternalApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ExternalApiService externalApiService;
    @Override
    public String create(Long userId, PostCreateRequestDto postCreateRequestDto, MultipartFile image) {
        try {
            User user = findUserOrThrow(userId);
            if (image.isEmpty()) {
                return "이미지를 업로드해주세요.";
            }
            log.info("이미지 업로드 시작: {}", image);

            String response = externalApiService.sendImageToFlask(image);
            log.info("Flask API Response: {}", response);
            boolean isValid = response.contains("true");
            if (isValid) {
                String imageUrl = s3Service.uploadFile("user/" + userId, image);
                Post post = PostConverter.toEntity(
                    postCreateRequestDto.getTitle(),
                    postCreateRequestDto.getContent(),
                    imageUrl,
                    user
                );

                Long postId = postRepository.save(post).getId();
                return "게시글 생성 완료 (ID: " + postId + ")";
            }
            return "이미지 검증에 실패했습니다. 올바른 이미지를 업로드해주세요.";
        }
        catch (Exception e) {
            log.error("게시글 생성 중 오류 발생: {}", e.getMessage());
            return "이미지 검증에 실패했습니다.";
        }
    }


    @Override
    public List<PostGetResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByUpdatedAtDesc();
        return posts.stream()
                .map(post -> PostGetResponseDto.builder()
                        .id(post.getId())
                        .userName(post.getUser().getUsername())
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl())
                        .likeCount(post.getLikeCount())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .toList();
    }


    @Override
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("없는 게시글: " + postId));
        Long currentLikeCount = post.getLikeCount();
        if (currentLikeCount == null) {
            currentLikeCount = 0L;
        }
        post.setLikeCount(currentLikeCount + 1);
        postRepository.save(post);
    }

    @Override
    public List<PostGetResponseDto> getLikePosts() {
        List<Post> posts = postRepository.findTop5ByOrderByLikeCountDesc();
        return posts.stream()
                .map(post -> PostGetResponseDto.builder()
                        .id(post.getId())
                        .userName(post.getUser().getUsername())
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl())
                        .likeCount(post.getLikeCount())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .toList();
    }

    @Override
    public List<PostGetResponseDto> getMyPosts(Long userId) {
        User user = findUserOrThrow(userId);
        List<Post> posts = postRepository.findAllByUserOrderByUpdatedAtDesc(user);
        return posts.stream()
                .map(post -> PostGetResponseDto.builder()
                        .id(post.getId())
                        .userName(post.getUser().getUsername())
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl())
                        .likeCount(post.getLikeCount())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .toList();
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));
    }



}
