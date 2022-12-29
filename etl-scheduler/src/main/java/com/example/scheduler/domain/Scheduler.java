package com.example.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.context.annotation.Requires;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "schedulers",
        indexes = {@Index(name = "pipeline_id_schedulers_index", columnList = "pipelineId")}
        )
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="scheduler", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<JobRun> runs = new ArrayList<>();

    @NotNull
    private Long pipelineId;
    @Enumerated(EnumType.ORDINAL)
    private SchedulerType schedulerType;
    @Column(nullable = true)
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
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition =
            "timestamp without time zone default (now() at time zone('utc'))",
            insertable = false, updatable = false)
//    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
//    @Column(columnDefinition = "timestamp without time zone default (now() at time zone('utc'))")
//    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;
    @NotNull
    private boolean deleted;
}
