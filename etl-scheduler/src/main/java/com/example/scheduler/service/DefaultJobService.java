package com.example.scheduler.service;

import com.example.scheduler.client.JobPipelineClient;
import com.example.scheduler.mapping.JobPipelineSchedulerMapper;
import com.example.scheduler.model.JobPipelineSchedulerModel;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.repository.JobPipelineSchedulerRepository;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Primary
@Singleton
public class DefaultJobService implements JobService {
    @Inject
    private JobPipelineSchedulerRepository repository;

    private final JobPipelineSchedulerMapper mapper = JobPipelineSchedulerMapper.MAPPER;

    @Override
    public Optional<JobPipelineSchedulerModel> getById(Long id) {
        var result = repository.findById(id).map(mapper::toModel);
        return result;
    }

    @Override
    public List<JobPipelineSchedulerModel> getAllBySchedulerId(Long id) {
        var domains = repository.findAllBySchedulerId(id);
        var result = mapper.toModel(domains);
        return result;
    }

    @Override
    public List<JobPipelineSchedulerModel> getAll() {
        var domains = repository.findAll();
        var result = mapper.toModel(domains);
        return result;
    }
}
