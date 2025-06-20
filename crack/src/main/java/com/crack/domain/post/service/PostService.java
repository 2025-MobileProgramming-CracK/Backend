package com.crack.domain.post.service;

import com.crack.domain.post.dto.request.PostCreateRequestDto;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

  Long create(Long userId, PostCreateRequestDto postCreateRequestDto, MultipartFile image);
}
