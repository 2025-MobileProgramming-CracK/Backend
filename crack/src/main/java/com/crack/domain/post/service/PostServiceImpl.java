package com.crack.domain.post.service;


import com.crack.domain.post.converter.PostConverter;
import com.crack.domain.post.dto.request.PostCreateRequestDto;
import com.crack.domain.post.entity.Post;
import com.crack.domain.post.repository.PostRepository;
import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.global.config.aws.S3Service;
import lombok.AllArgsConstructor;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저: " + userId));
        String imageUrl = s3Service.uploadFile("user/" + userId, image);
        Post post =PostConverter.toEntity(
            postCreateRequestDto.getTitle(),
            postCreateRequestDto.getContent(),
            imageUrl,
            user);
        return postRepository.save(post).getId();
    }

}
