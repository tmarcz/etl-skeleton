package com.example.service;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.example.mapping.PipelineMapper;
import com.example.model.PipelineModel;
import com.example.repository.PipelineRepository;
import java.util.Optional;

@Primary
@Singleton
public class DefaultPipelineService implements PipelineService {
    @Inject
    private PipelineRepository repository;
    private final PipelineMapper mapper = PipelineMapper.MAPPER;

    @Override
    public Optional<PipelineModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }
}
