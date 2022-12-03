package com.example.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pipelines_schedulers", uniqueConstraints={@UniqueConstraint(columnNames = {"pipelineId" , "cron"})})
public class PipelineScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long pipelineId;
    @Enumerated(EnumType.ORDINAL)
    private SchedulerType schedulerType;
    private String cron;

    @Column(nullable = true)
    private LocalDateTime startDate;
    @Column(nullable = true)
    private LocalDateTime endDate;
    @NotNull
    private boolean active;
    @NotNull
    private boolean running;
    @Column(nullable = true)
    private LocalDateTime nextRunDate;
    @Column(nullable = true)
    private LocalDateTime lastRunDate;
    private boolean lastRunSuccessStatus;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @NotNull
    private boolean deleted;
}
