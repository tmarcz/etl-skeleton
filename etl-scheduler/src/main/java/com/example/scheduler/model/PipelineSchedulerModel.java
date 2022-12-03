package com.example.scheduler.model;

import com.example.scheduler.domain.SchedulerType;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Introspected
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineSchedulerModel {
    private Long id;

    private Long pipelineId;
    private SchedulerTypeModel schedulerType;
    private String cron;

    private Date startDate;
    private Date endDate;
    private boolean active;
    private boolean running;
    private Date nextRunDate;
    private Date lastRunDate;
    private boolean lastRunSuccessStatus;
    private Date createdDate;
    private boolean deleted;
}
