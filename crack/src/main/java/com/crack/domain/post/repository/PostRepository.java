package com.crack.domain.post.repository;

import com.crack.domain.calendar.entity.Calendar;
import com.crack.domain.post.entity.Post;
import com.crack.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findAllByOrderByUpdatedAtDesc();
  List<Post> findTop5ByOrderByLikeCountDesc();
  List<Post> findAllByUserOrderByUpdatedAtDesc(User user);
}
