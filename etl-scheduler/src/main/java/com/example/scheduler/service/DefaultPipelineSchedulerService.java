package com.example.scheduler.service;

import com.example.scheduler.client.JobPipelineClient;
import com.example.scheduler.domain.JobPipelineScheduler;
import com.example.scheduler.mapping.PipelineSchedulerMapper;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.repository.JobPipelineSchedulerRepository;
import com.example.scheduler.repository.PipelineSchedulerRepository;
import io.micronaut.context.annotation.Primary;
import io.micronaut.transaction.interceptor.TransactionalInterceptor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

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

    @Override
    @Transactional
    public boolean runFirst() {
        var currentTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(0));
        var domain = repository.findFirstJobToRunForUpdate(currentTimestamp);

        var threadId = Thread.currentThread().getId();
        System.out.println("# CHECK Thread: " + threadId);

        var result = domain.isPresent();
        if (result) {
            var id = domain.get().getId();
            System.out.println("# RESULT Thread: " + threadId + "\t # Running job: " + id);

            var job = new JobPipelineScheduler(null, id, null, null);
            job = jobPipelineSchedulerRepository.save(job);

            jobPipelineClient.start(domain.get().getPipelineId(), id, job.getId());

            domain.get().setRunning(true);
            entityManager.merge(domain.get());
            jobPipelineSchedulerRepository.flush();

            System.out.println("# end #");
        }

//        TransactionalInterceptor.currentTransactionStatus().setRollbackOnly();

        return result;
    }

    @Override
    public void runAll() {
        boolean result;
        do {
            result = runFirst();
        } while (result);
    }

    @Override
    public void runAllAsync(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        Arrays.setAll(threads, i -> new Thread(this::runAll));
        Arrays.stream(threads).forEach(Thread::start);
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
    }

    @Override
    public PipelineSchedulerModel create(PipelineSchedulerModel model) {
        var domain = mapper.toDomain(model);
        domain = repository.save(domain);
        var result = mapper.toModel(domain);
        return result;
    }

    @Override
    public Optional<PipelineSchedulerModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }
}
