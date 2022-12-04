package com.example.etl.job.repository;

import com.example.etl.job.domain.JobPipeline;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface JobPipelineRepository extends JpaRepository<JobPipeline, Long> {
}
