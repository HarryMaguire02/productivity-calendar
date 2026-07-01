package com.productivity.backend.calendar;

import com.productivity.backend.entity.PlannedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PlannedEventRepository extends JpaRepository<PlannedEvent, UUID> {
    List<PlannedEvent> findByUserIdAndStartTimeBetween(UUID userId, Instant from, Instant to);
}
