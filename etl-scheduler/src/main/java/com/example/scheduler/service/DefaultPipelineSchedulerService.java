package com.example.scheduler.service;

import com.example.scheduler.client.JobPipelineClient;
import com.example.scheduler.domain.JobRun;
import com.example.scheduler.domain.Scheduler;
import com.example.scheduler.domain.SchedulerType;
import com.example.scheduler.mapping.JobPipelineSchedulerMapper;
import com.example.scheduler.mapping.PipelineSchedulerMapper;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.repository.JobPipelineSchedulerRepository;
import com.example.scheduler.repository.PipelineSchedulerRepository;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            var threadId = Thread.currentThread().getId();
            System.out.println("# CHECK Thread: " + threadId);
            System.out.println("# RESULT Thread: " + threadId + "\t # Running job: " + scheduler.getId());

            var job = new JobRun(null, scheduler, scheduler.getPipelineId(), LocalDateTime.now(), null);
            job = jobPipelineSchedulerRepository.save(job);

            jobPipelineClient.start(scheduler.getPipelineId(), scheduler.getId(), job.getId());

            scheduler.setRunning(true);
            entityManager.merge(scheduler);
            jobPipelineSchedulerRepository.flush();

            System.out.println("# end #");
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

        var threadId = Thread.currentThread().getId();
        System.out.println("# CHECK Thread: " + threadId);

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
    public void runAllAsync(int threadCount) {
        try {
            Thread[] threads = new Thread[threadCount];
            Arrays.setAll(threads, i -> new Thread(this::runAll));
            Arrays.stream(threads).forEach(Thread::start);
            for (int i = 0; i < threadCount; i++) {
                threads[i].join();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
    public PipelineSchedulerModel create(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        domain = repository.saveAndFlush(domain);
        domain = repository.findById(domain.getId()).get();
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    public PipelineSchedulerModel createCron(PipelineSchedulerModel model) {
        return null;
    }

    @Override
    public PipelineSchedulerModel createInstant(PipelineSchedulerModel model) {
        return null;
    }

    @Override
    public PipelineSchedulerModel createCalendar(PipelineSchedulerModel model) {
        return null;
    }

    @Override
    public PipelineSchedulerModel update(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        domain = repository.update(domain);
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    public PipelineSchedulerModel updateCron(PipelineSchedulerModel model) {
        return null;
    }

    @Override
    public PipelineSchedulerModel updateCalendar(PipelineSchedulerModel model) {
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
