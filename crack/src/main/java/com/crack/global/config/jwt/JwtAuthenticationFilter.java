package com.crack.global.config.jwt;

import com.crack.domain.user.entity.User;
import com.crack.domain.user.repository.UserRepository;
import com.crack.domain.user.service.UserService;
import com.crack.global.config.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;


  public JwtAuthenticationFilter(JwtUtil jwtUtil ,UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository=userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    String token = null;
    String email = null;

    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
      token = authorizationHeader.substring(7);
      try {
        email = jwtUtil.extractUseremail(token);
      } catch (Exception e) {
        System.out.println("Invalid JWT token: " + e.getMessage());
      }
    }

    if(email != null && jwtUtil.isTokenValid(token, email)) {
      User user = userRepository.findUserByEmail(email)
          .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

      CustomUserDetails userDetails = new CustomUserDetails(user);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }


}
