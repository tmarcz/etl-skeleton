package com.example.service;

import com.example.model.PipelineModel;

import java.util.Optional;

public interface PipelineService {
    Optional<PipelineModel> getById(Long id);
}
