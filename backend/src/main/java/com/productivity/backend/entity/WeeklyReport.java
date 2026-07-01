package com.productivity.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "weekly_reports")
public class WeeklyReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate weekStart;

    @Column(nullable = false)
    private LocalDate weekEnd;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    private Integer aiPromptTokens;

    private Integer aiResponseTokens;

    @Column(length = 500)
    private String pdfPath;

    private Instant generatedAt;
}
