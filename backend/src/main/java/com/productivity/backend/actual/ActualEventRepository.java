package com.productivity.backend.actual;

import com.productivity.backend.entity.ActualEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ActualEventRepository extends JpaRepository<ActualEvent, UUID> {
    List<ActualEvent> findByUserIdAndStartTimeBetween(UUID userId, Instant from, Instant to);
}
