package com.example.scheduler.model;

import com.example.scheduler.domain.Scheduler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JobPipelineSchedulerModel {
    private Long id;

    private PipelineSchedulerModel scheduler;

    private Long pipelineId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
