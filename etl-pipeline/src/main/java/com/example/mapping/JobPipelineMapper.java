package com.example.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.domain.JobPipeline;
import com.example.model.JobPipelineModel;

@Mapper
public interface JobPipelineMapper {
    JobPipelineMapper MAPPER = Mappers.getMapper(JobPipelineMapper.class);

    JobPipeline toDomain(JobPipelineModel s);
    JobPipelineModel toModel(JobPipeline s);
}
