package com.example.mapping;

import com.example.domain.Pipeline;
import com.example.model.PipelineModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PipelineMapper {
    PipelineMapper MAPPER = Mappers.getMapper(PipelineMapper.class);

    Pipeline toDomain(PipelineModel s);
    PipelineModel toModel(Pipeline s);
}
