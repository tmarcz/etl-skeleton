package com.example.scheduler.mapping;

import com.example.scheduler.domain.JobPipelineScheduler;
import com.example.scheduler.domain.PipelineScheduler;
import com.example.scheduler.model.PipelineSchedulerModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PipelineSchedulerMapper {
    PipelineSchedulerMapper MAPPER = Mappers.getMapper(PipelineSchedulerMapper.class);

    PipelineScheduler toDomain(PipelineSchedulerModel s);
    PipelineSchedulerModel toModel(PipelineScheduler s);
}
