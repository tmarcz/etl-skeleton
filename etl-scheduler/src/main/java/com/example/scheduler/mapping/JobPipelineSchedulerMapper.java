package com.example.scheduler.mapping;

import com.example.scheduler.domain.JobRun;
import com.example.scheduler.domain.Scheduler;
import com.example.scheduler.model.JobPipelineSchedulerModel;
import com.example.scheduler.model.PipelineSchedulerModel;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JobPipelineSchedulerMapper {
    JobPipelineSchedulerMapper MAPPER = Mappers.getMapper(JobPipelineSchedulerMapper.class);

    JobRun toDomain(JobPipelineSchedulerModel s);

    @Mappings({
            @Mapping(source = "scheduler.id", target = "schedulerId"),
            @Mapping(target = "scheduler", ignore = true)
    })
    JobPipelineSchedulerModel toModel(JobRun s);

    @IterableMapping(elementTargetType = JobPipelineSchedulerModel.class)
    List<JobPipelineSchedulerModel> toModel(List<JobRun> payment);
}
