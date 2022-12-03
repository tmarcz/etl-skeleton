package com.example.scheduler;

import com.example.scheduler.domain.PipelineScheduler;
import com.example.scheduler.domain.SchedulerType;
import com.example.scheduler.repository.PipelineSchedulerRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Singleton
@Requires(notEnv = "mock")
@Slf4j
public class DataInitializer implements ApplicationEventListener<ServerStartupEvent> {
    @Inject
    private PipelineSchedulerRepository pipelineSchedulerRepository;
    @Inject
    private TransactionOperations<?> tx;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        log.info("initializing sample data...");

        var data = List.of(
                // next run close to current date time
                PipelineScheduler.builder()
                        .pipelineId(1L)
                        .schedulerType(SchedulerType.CRON)
                        .cron("0/10 0 0 ? * * *") // every 10 seconds
                        .active(true)
                        .running(false)
                        .nextRunDate(LocalDateTime.now())
                        .deleted(false).build(),

                // next run is 10 days old
                PipelineScheduler.builder()
                        .pipelineId(1L)
                        .schedulerType(SchedulerType.CRON)
                        .cron("0/10 0 0 ? * * *") // every 10 seconds
                        .active(true)
                        .running(false)
                        .nextRunDate(LocalDateTime.now().minusDays(10))
                        .deleted(false).build(),

                // next run in 5 minutes
                PipelineScheduler.builder()
                        .pipelineId(1L)
                        .schedulerType(SchedulerType.CRON)
                        .cron("0/10 0 0 ? * * *") // every 10 seconds
                        .active(true)
                        .running(false)
                        .nextRunDate(LocalDateTime.now().plusMinutes(5))
                        .deleted(false).build(),

                // next run in 1 day
                PipelineScheduler.builder()
                        .pipelineId(1L)
                        .schedulerType(SchedulerType.CRON)
                        .cron("0/10 0 0 ? * * *") // every 10 seconds
                        .active(true)
                        .running(false)
                        .nextRunDate(LocalDateTime.now().plusDays(1))
                        .deleted(false).build()
        );
        tx.executeWrite(status -> {
            this.pipelineSchedulerRepository.deleteAll();
            this.pipelineSchedulerRepository.saveAll(data);
            return null;
        });
        tx.executeRead(status -> {
            this.pipelineSchedulerRepository.findAll().forEach(p -> log.info("saved post: {}", p));
            return null;
        });

        log.info("data initialization is done...");
    }
}
