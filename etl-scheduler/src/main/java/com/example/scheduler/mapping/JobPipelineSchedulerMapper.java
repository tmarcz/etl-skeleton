package com.example.scheduler.mapping;

import com.example.scheduler.domain.JobPipelineScheduler;
import com.example.scheduler.model.JobPipelineSchedulerModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobPipelineSchedulerMapper {
    JobPipelineSchedulerMapper MAPPER = Mappers.getMapper(JobPipelineSchedulerMapper.class);

    JobPipelineScheduler toDomain(JobPipelineSchedulerModel s);
    JobPipelineSchedulerModel toModel(JobPipelineScheduler s);
}
