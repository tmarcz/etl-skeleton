package com.example.scheduler.mapping;

import com.example.scheduler.domain.PipelineScheduler;
import com.example.scheduler.domain.SchedulerType;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.model.SchedulerTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchedulerTypeMapper {
    SchedulerTypeMapper MAPPER = Mappers.getMapper(SchedulerTypeMapper.class);

    SchedulerType toDomain(SchedulerTypeModel s);
    SchedulerTypeModel toModel(SchedulerType s);
}
