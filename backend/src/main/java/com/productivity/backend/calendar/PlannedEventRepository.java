package com.productivity.backend.calendar;

import com.productivity.backend.calendar.dto.PlannedEventResponse;
import com.productivity.backend.entity.PlannedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlannedEventRepository extends JpaRepository<PlannedEvent, UUID> {
    Optional<PlannedEvent> findByIdAndUserId(UUID id,  UUID userId);
    List<PlannedEvent> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(UUID userId, Instant from, Instant to);

    @Query("""
            SELECT e from PlannedEvent e
            WHERE e.user.id = :userId
            AND e.startTime < :endTime
            AND e.endTime > :startTime
            AND (:excludeId is NULL OR e.id <> :excludeId)
            """)
    List<PlannedEvent> findConflicts(
            @Param("userId") UUID userId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("excludeId") UUID excludeId
    );
}
