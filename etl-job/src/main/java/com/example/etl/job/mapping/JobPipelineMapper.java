package com.example.etl.job.mapping;

import com.example.etl.job.domain.JobPipeline;
import com.example.etl.job.model.JobPipelineModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobPipelineMapper {
    JobPipelineMapper MAPPER = Mappers.getMapper(JobPipelineMapper.class);

    JobPipeline toDomain(JobPipelineModel s);
    JobPipelineModel toModel(JobPipeline s);
}
