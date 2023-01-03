package com.example.scheduler.job;

//import com.example.scheduler.service.SchedulerService;
import com.example.scheduler.service.PipelineSchedulerService;
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
    public static final int RATE_MILLISECONDS = 100;
    private boolean active = true;

    @Inject
    private PipelineSchedulerService pipelineSchedulerService;

    @Scheduled(fixedRate = RATE_MILLISECONDS + "ms")
    void execute() throws InterruptedException {
        if (!active) return;

        pipelineSchedulerService.runFirst();
    }

    public void forceExecute() {
        pipelineSchedulerService.runFirst();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
