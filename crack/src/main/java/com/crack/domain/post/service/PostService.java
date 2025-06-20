package com.crack.domain.post.service;

import com.crack.domain.post.dto.request.PostCreateRequestDto;
import com.crack.domain.post.dto.response.PostGetResponseDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

  Long create(Long userId, PostCreateRequestDto postCreateRequestDto, MultipartFile image);

  List<PostGetResponseDto> getAllPosts();
}
