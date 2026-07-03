package com.productivity.backend.actual;

import com.productivity.backend.entity.ActualEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActualEventRepository extends JpaRepository<ActualEvent, UUID> {
    List<ActualEvent> findByUserIdAndStartTimeBetween(UUID userId, Instant from, Instant to);
    Optional<ActualEvent> findByIdAndUserId(UUID id, UUID userId);
    List<ActualEvent> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(UUID userId, Instant start, Instant end);
}
