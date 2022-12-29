package com.example.scheduler.repository;

import com.example.scheduler.domain.JobRun;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface JobPipelineSchedulerRepository  extends JpaRepository<JobRun, Long> {
}
