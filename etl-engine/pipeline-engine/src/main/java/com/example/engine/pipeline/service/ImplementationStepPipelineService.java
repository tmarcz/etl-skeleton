package com.example.engine.pipeline.service;

import com.example.engine.pipeline.model.ImplementationStepPipelineModel;

import java.util.Optional;

public interface ImplementationStepPipelineService {
    Optional<ImplementationStepPipelineModel> getById(Long id);
}
