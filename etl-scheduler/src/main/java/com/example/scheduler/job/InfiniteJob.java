package com.example.scheduler.job;

import com.example.scheduler.service.SchedulerService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class InfiniteJob {
    private static final Logger LOG = LoggerFactory.getLogger(InfiniteJob.class);

    @Inject
    private SchedulerService schedulerService;

    @Scheduled(fixedRate = "5s")
    void execute() throws InterruptedException {
        System.out.println("Simple Job every 5 seconds:" + new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));

//        int cores = Runtime.getRuntime().availableProcessors();
//        schedulerService.runAllAsync(cores);
    }
}