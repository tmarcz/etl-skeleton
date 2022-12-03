package com.example.repository;

import com.example.domain.Pipeline;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface PipelineRepository  extends JpaRepository<Pipeline, Long> {
}
