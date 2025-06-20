package com.crack.domain.post.converter;


import com.crack.domain.post.entity.Post;
import com.crack.domain.user.entity.User;

public class PostConverter {
  public static Post toEntity(String title, String content, String imageUrl,User user) {
    return Post.builder()
        .title(title)
        .imageUrl(imageUrl)
        .content(content)
        .user(user)
        .build();
  }
}
