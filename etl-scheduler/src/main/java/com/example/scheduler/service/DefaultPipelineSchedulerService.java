package com.example.scheduler.service;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.example.scheduler.client.JobPipelineClient;
import com.example.scheduler.domain.JobRun;
import com.example.scheduler.domain.Scheduler;
import com.example.scheduler.domain.SchedulerType;
import com.example.scheduler.mapping.JobPipelineSchedulerMapper;
import com.example.scheduler.mapping.PipelineSchedulerMapper;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.model.SchedulerTypeModel;
import com.example.scheduler.repository.JobPipelineSchedulerRepository;
import com.example.scheduler.repository.PipelineSchedulerRepository;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.cronutils.model.CronType.QUARTZ;

@Primary
@Singleton
public class DefaultPipelineSchedulerService implements PipelineSchedulerService {
    @Inject
    private PipelineSchedulerRepository repository;
    @Inject
    private JobPipelineSchedulerRepository jobPipelineSchedulerRepository;
    @Inject
    private JobPipelineClient jobPipelineClient;
    @Inject
    private EntityManager entityManager;

    private final PipelineSchedulerMapper mapper = PipelineSchedulerMapper.MAPPER;
    private final JobPipelineSchedulerMapper jobMapper = JobPipelineSchedulerMapper.MAPPER;

    @Transactional
    public JobRun run(Scheduler scheduler) {
        try {
            var job = new JobRun(null, scheduler, scheduler.getPipelineId(), LocalDateTime.now(), null);
            job = jobPipelineSchedulerRepository.save(job);

            jobPipelineClient.start(scheduler.getPipelineId(), scheduler.getId(), job.getId());

            scheduler.setRunning(true);
            entityManager.merge(scheduler);
            jobPipelineSchedulerRepository.flush();

            return job;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @Transactional
    public Optional<PipelineSchedulerModel> runFirst() {
        Optional<PipelineSchedulerModel> result = Optional.empty();
        var currentTimestamp = LocalDateTime.now();
        var domain = repository.findFirstJobToRunForUpdate(currentTimestamp);

        if (domain.isPresent()) {
            var job = run(domain.get());
            domain.get().setRunning(true);
            repository.flush();
            result = Optional.of(mapper.toModel(domain.get()));
            result.get().setRuns(List.of(jobMapper.toModel(job)));
        }
        return result;
    }

    @Override
    public List<PipelineSchedulerModel> runAll() {
        var result = new ArrayList<PipelineSchedulerModel>();

        Optional<PipelineSchedulerModel> scheduler;
        do {
            scheduler = runFirst();
            scheduler.ifPresent(result::add);
        } while (scheduler.isPresent());

        return result;
    }

    @Override
    public List<PipelineSchedulerModel> runAllAsync(int threadCount) {
        var runAllFutures = IntStream.range(0, threadCount)
                .mapToObj(p -> CompletableFuture.supplyAsync(this::runAll))
                .collect(Collectors.toList());

        var allFutures = CompletableFuture.allOf(runAllFutures.toArray(new CompletableFuture[0]));

        var allRunAllFuture = allFutures.thenApply(v ->
                runAllFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        var result = allRunAllFuture.join().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return result;
    }

    @Override
    @Transactional
    public void complete(int jobId, boolean success) {
//        var completeDateTime = LocalDateTime.now();
//        var scheduler = repository.findById()
//        repository.saveAndFlush();
        // TODO: 1. update pipeline schedule id
        // TODO: 2. update pipeline schedule job id
    }

    @Override
    @Transactional
    public PipelineSchedulerModel instantRun(long pipelineId) {
        var domain = Scheduler.builder()
                .pipelineId(pipelineId)
                .schedulerType(SchedulerType.INSTANT)
                .nextRunDate(LocalDateTime.now())
                .active(true)
                .build();
        domain = repository.saveAndFlush(domain);
        var job = run(domain);
        var result = mapper.toModel(domain);
        result.setRuns(List.of(jobMapper.toModel(job)));
        return result;
    }

    @Override
    public LocalDateTime getNextCronExecutionDateTimeFromNow(String expression) {
        var cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        var parser = new CronParser(cronDefinition);
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var executionTime = ExecutionTime.forCron(parser.parse(expression));
        var nextExecution = executionTime.nextExecution(now);
        var result = nextExecution.map(ZonedDateTime::toLocalDateTime).orElse(null);
        return result;
    }

    @Override
    @Transactional
    public PipelineSchedulerModel complete(Long id) {
        var domain = repository.findById(id).get();
        var job = jobPipelineSchedulerRepository.findBySchedulerIdAndEndDateIsNull(domain.getId());
        var now = LocalDateTime.now();
        job.setEndDate(now);
        job = jobPipelineSchedulerRepository.saveAndFlush(job);

        if (domain.getSchedulerType() == SchedulerType.CRON) {
            var nextExecution = getNextCronExecutionDateTimeFromNow(domain.getCron());
            domain.setNextRunDate(nextExecution);
        } else if (domain.getSchedulerType() == SchedulerType.INSTANT) {
            domain.setActive(false);
            domain.setLastRunDate(domain.getNextRunDate());
            domain.setNextRunDate(null);
        } else {
            // TODO: to implement calendar scheduling
            throw new RuntimeException("TODO: not yet implemented!");
        }
        domain = repository.saveAndFlush(domain);

        var result = mapper.toModel(domain);
        result.setRuns(List.of(jobMapper.toModel(job)));
        return result;
    }

    @Override
    public PipelineSchedulerModel create(PipelineSchedulerModel model) {
        PipelineSchedulerModel result;
        if (model.getSchedulerType() == SchedulerTypeModel.CRON) {
            result = createCron(model);
        } else if (model.getSchedulerType() == SchedulerTypeModel.INSTANT) {
            result = createInstant(model);
        } else {
            result = createCalendar(model);
        }
        return result;
    }

    @Override
    public PipelineSchedulerModel createCron(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        var nextExecution = getNextCronExecutionDateTimeFromNow(domain.getCron());
        domain.setNextRunDate(nextExecution);
        domain = repository.saveAndFlush(domain);
        domain = repository.findById(domain.getId()).get();
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    public PipelineSchedulerModel createInstant(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        domain.setNextRunDate(LocalDateTime.now());
        domain = repository.saveAndFlush(domain);
        domain = repository.findById(domain.getId()).get();
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    public PipelineSchedulerModel createCalendar(PipelineSchedulerModel model) {
        // TODO: to implement calendar logic
        var domain = mapper.toDomain(model);
        domain = repository.saveAndFlush(domain);
        domain = repository.findById(domain.getId()).get();
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    @Transactional
    public PipelineSchedulerModel update(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        domain = repository.update(domain);
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    @Transactional
    public PipelineSchedulerModel updateCron(PipelineSchedulerModel model) {
        // TODO: to implement cron logic
        // update next run if cron changed
        return null;
    }

    @Override
    @Transactional
    public PipelineSchedulerModel updateCalendar(PipelineSchedulerModel model) {
        // TODO: to implement calendar logic
        // update next run if calendar changed
        return null;
    }

    @Override
    public Optional<PipelineSchedulerModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }

    @Override
    public List<PipelineSchedulerModel> getAll() {
        var domains = repository.findAll();
        var result = mapper.toModel(domains);
        return result;
    }

    @Override
    public PipelineSchedulerModel delete(Long id) {
        var domain = repository.findById(id);
        domain.get().setDeleted(true);
        repository.flush();
        var result = mapper.toModel(domain.get());
        return result;
    }


}
