package com.example.scheduler.repository;

import com.example.scheduler.domain.JobPipelineScheduler;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface JobPipelineSchedulerRepository  extends JpaRepository<JobPipelineScheduler, Long> {
}
