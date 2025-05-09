package com.crack.domain.user.repository;

import com.crack.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String email);

}
