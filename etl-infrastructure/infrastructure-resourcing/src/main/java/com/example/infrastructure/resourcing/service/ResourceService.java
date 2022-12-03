package com.example.infrastructure.resourcing.service;

import com.example.infrastructure.resourcing.model.ResourceModel;

import java.util.Optional;

public interface ResourceService {
    Optional<ResourceModel> getById(Long id);
}
