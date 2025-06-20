package com.crack.domain.calendar.repository;

import com.crack.domain.calendar.entity.Calendar;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
  Calendar findByUserId(Long userId);

  @Query("""
    SELECT c 
    FROM Calendar c 
    WHERE c.user.id = :userId 
    AND c.date BETWEEN :startOfDay AND :endOfDay
""")
  List<Calendar> findAllByUserIdAndDateBetween(
      @Param("userId") Long userId,
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay
  );

}
