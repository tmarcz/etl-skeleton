package com.example.scheduler.mapping;

import com.example.scheduler.domain.Scheduler;
import com.example.scheduler.model.PipelineSchedulerModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = JobPipelineSchedulerMapper.class)
public interface PipelineSchedulerMapper {
    PipelineSchedulerMapper MAPPER = Mappers.getMapper(PipelineSchedulerMapper.class);

    Scheduler toDomain(PipelineSchedulerModel s);

//    @Mappings({
//            @Mapping(target = "runs", ignore = true)
//    })
//    @Named("toModel")
    @Mapping(target = "runs", ignore = true)
    PipelineSchedulerModel toModel(Scheduler s);

    @IterableMapping(elementTargetType = PipelineSchedulerModel.class)
    List<PipelineSchedulerModel> toModel(List<Scheduler> payment);
}
