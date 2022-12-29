package com.example.scheduler.mapping;

import com.example.scheduler.domain.JobRun;
import com.example.scheduler.model.JobPipelineSchedulerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobPipelineSchedulerMapper {
    JobPipelineSchedulerMapper MAPPER = Mappers.getMapper(JobPipelineSchedulerMapper.class);

    JobRun toDomain(JobPipelineSchedulerModel s);

    @Mappings({
            @Mapping(target = "scheduler", ignore = true)
    })
    JobPipelineSchedulerModel toModel(JobRun s);
}
