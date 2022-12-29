package com.example.scheduler.model;

import com.example.scheduler.domain.JobRun;
import com.example.scheduler.domain.SchedulerType;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Introspected
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PipelineSchedulerModel {
    private Long id;

    @Builder.Default
    private List<JobPipelineSchedulerModel> runs = new ArrayList<>();

    private Long pipelineId;
    private SchedulerTypeModel schedulerType;
    private String cron;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private boolean running;
    private LocalDateTime nextRunDate;
    private LocalDateTime lastRunDate;
    private boolean lastRunSuccessStatus;

    private LocalDateTime createdDate;
    private boolean deleted;

    public void setNextRunDate(LocalDateTime nextRunDate) {
        this.nextRunDate = nextRunDate.truncatedTo(ChronoUnit.MICROS);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate.truncatedTo(ChronoUnit.MICROS);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate.truncatedTo(ChronoUnit.MICROS);
    }

    public void setLastRunDate(LocalDateTime lastRunDate) {
        this.lastRunDate = lastRunDate.truncatedTo(ChronoUnit.MICROS);
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate.truncatedTo(ChronoUnit.MICROS);
    }

    public static class PipelineSchedulerModelBuilder {
        public PipelineSchedulerModelBuilder nextRunDate(LocalDateTime nextRunDate) {
            this.nextRunDate = nextRunDate == null ? null : nextRunDate.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public PipelineSchedulerModelBuilder startDate(LocalDateTime startDate) {
            this.startDate = startDate == null ? null : startDate.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public PipelineSchedulerModelBuilder endDate(LocalDateTime endDate) {
            this.endDate = endDate == null ? null : endDate.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public PipelineSchedulerModelBuilder lastRunDate(LocalDateTime lastRunDate) {
            this.lastRunDate = lastRunDate == null ? null : lastRunDate.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public PipelineSchedulerModelBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate == null ? null : createdDate.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}
