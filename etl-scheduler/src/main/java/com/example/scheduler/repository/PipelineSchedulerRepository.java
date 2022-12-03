package com.example.scheduler.repository;

import com.example.scheduler.domain.PipelineScheduler;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface PipelineSchedulerRepository extends JpaRepository<PipelineScheduler, Long> {
    @Query(value =
            "select * from demo.pipelines_schedulers " +
            "where active = true " +
            "   and running = false " +
            "   and scheduler_type = 0 " +
            "   and next_run_date < :dateTime " +
            "order by next_run_date asc " +
            "limit 1 " +
            "for update skip locked",
            nativeQuery = true)
    Optional<PipelineScheduler> findFirstJobToRunForUpdate(Timestamp dateTime);
}
