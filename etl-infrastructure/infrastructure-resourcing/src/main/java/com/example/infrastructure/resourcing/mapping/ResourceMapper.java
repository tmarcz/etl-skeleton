package com.example.infrastructure.resourcing.mapping;

import com.example.infrastructure.resourcing.domain.Resource;
import com.example.infrastructure.resourcing.model.ResourceModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResourceMapper {
    ResourceMapper MAPPER = Mappers.getMapper(ResourceMapper.class);

    Resource toDomain(ResourceModel s);
    ResourceModel toModel(Resource s);
}
