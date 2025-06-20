package com.crack.domain.post.service;


import com.crack.domain.post.converter.PostConverter;
import com.crack.domain.post.dto.request.PostCreateRequestDto;
import com.crack.domain.post.dto.response.PostGetResponseDto;
import com.crack.domain.post.entity.Post;
import com.crack.domain.post.repository.PostRepository;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.global.config.aws.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    public Long create(Long userId, PostCreateRequestDto postCreateRequestDto, MultipartFile image) {
        User user =findUserOrThrow(userId);

        String imageUrl = s3Service.uploadFile("user/" + userId, image);
        Post post =PostConverter.toEntity(
            postCreateRequestDto.getTitle(),
            postCreateRequestDto.getContent(),
            imageUrl,
            user);
        return postRepository.save(post).getId();
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
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Override
    public List<PostGetResponseDto> getLatestPosts(Long postId) {
        List<Post> posts = postRepository.findTop5ByIdLessThanOrderByLikeCountDesc(postId);
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
