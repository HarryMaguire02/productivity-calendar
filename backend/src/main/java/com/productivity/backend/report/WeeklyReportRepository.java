package com.productivity.backend.report;

import com.productivity.backend.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, UUID> {
    List<WeeklyReport> findByUserId(UUID userId);
    Optional<WeeklyReport> findByUserIdAndWeekStart(UUID userId, LocalDate weekStart);
}
