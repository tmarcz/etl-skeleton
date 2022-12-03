package com.example.infrastructure.resourcing.service;

import com.example.infrastructure.resourcing.mapping.ResourceMapper;
import com.example.infrastructure.resourcing.model.ResourceModel;
import com.example.infrastructure.resourcing.repository.ResourceRepository;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Primary
@Singleton
public class DefaultResourceService implements ResourceService {
    @Inject
    private ResourceRepository repository;
    private final ResourceMapper mapper = ResourceMapper.MAPPER;

    @Override
    public Optional<ResourceModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }
}
