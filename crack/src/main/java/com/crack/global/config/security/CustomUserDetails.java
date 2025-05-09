package com.crack.global.config.security;

import com.crack.domain.user.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
  private User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    String roles = user.getRole().toString();
    for(String role : roles.split(",")) {
      authorities.add(() -> role);
    }
    return authorities;
  }

  @Override
  public String getPassword() {return user.getPassword();}

  @Override
  public String getUsername() {return user.getEmail();}


  public Long getId() {return user.getId();} // Member 엔티티의 ID를 반환}

  @Override
  public boolean isAccountNonExpired() {return true;}

  @Override
  public boolean isAccountNonLocked() {return true;}

  @Override
  public boolean isCredentialsNonExpired() {return true;}

  @Override
  public boolean isEnabled() {return true;}

}
