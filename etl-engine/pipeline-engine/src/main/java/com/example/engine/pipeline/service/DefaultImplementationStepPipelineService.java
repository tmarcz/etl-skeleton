package com.example.engine.pipeline.service;

import com.example.engine.pipeline.mapping.ImplementationStepPipelineMapper;
import com.example.engine.pipeline.model.ImplementationStepPipelineModel;
import com.example.engine.pipeline.repository.ImplementationStepPipelineRepository;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Primary
@Singleton
public class DefaultImplementationStepPipelineService implements ImplementationStepPipelineService {
    @Inject
    private ImplementationStepPipelineRepository repository;
    private final ImplementationStepPipelineMapper mapper = ImplementationStepPipelineMapper.MAPPER;

    @Override
    public Optional<ImplementationStepPipelineModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }
}
