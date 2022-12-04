package com.example.engine.pipeline.repository;

import com.example.engine.pipeline.domain.ImplementationStepPipeline;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface ImplementationStepPipelineRepository  extends JpaRepository<ImplementationStepPipeline, Long> {
    @Executable
    ImplementationStepPipeline findByTitle(String title);
}
