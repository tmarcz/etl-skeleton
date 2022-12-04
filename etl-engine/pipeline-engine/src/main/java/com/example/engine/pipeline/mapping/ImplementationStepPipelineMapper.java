package com.example.engine.pipeline.mapping;

import com.example.engine.pipeline.domain.ImplementationStepPipeline;
import com.example.engine.pipeline.model.ImplementationStepPipelineModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImplementationStepPipelineMapper {
    ImplementationStepPipelineMapper MAPPER = Mappers.getMapper(ImplementationStepPipelineMapper.class);

    ImplementationStepPipeline toDomain(ImplementationStepPipelineModel s);
    ImplementationStepPipelineModel toModel(ImplementationStepPipeline s);
}
