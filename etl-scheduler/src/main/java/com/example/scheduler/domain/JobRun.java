package com.example.scheduler.domain;

import io.micronaut.context.annotation.Requires;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "jobs_runs",
        indexes = {
                @Index(name = "scheduler_id_index", columnList = "scheduler_id"),
                @Index(name = "pipeline_id_jobs_runs_index", columnList = "pipeline_id")}
                )
public class JobRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scheduler_id")
    @NotNull
    @ToString.Exclude
    private Scheduler scheduler;

    @Column(name = "pipeline_id", nullable = true)
    private Long pipelineId;
    @CreationTimestamp
    private LocalDateTime startDate;
    @Column(nullable = true)
    private LocalDateTime endDate;
}