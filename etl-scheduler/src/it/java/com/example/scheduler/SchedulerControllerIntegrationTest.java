package com.example.scheduler;

import com.example.scheduler.client.JobPipelineClient;
import com.example.scheduler.domain.JobRun;
import com.example.scheduler.domain.Scheduler;
import com.example.scheduler.domain.SchedulerType;
import com.example.scheduler.job.InfiniteJob;
import com.example.scheduler.mapping.PipelineSchedulerMapper;
import com.example.scheduler.model.JobPipelineSchedulerModel;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.model.SchedulerTypeModel;
import com.example.scheduler.repository.JobPipelineSchedulerRepository;
import com.example.scheduler.repository.PipelineSchedulerRepository;
import com.example.scheduler.service.DefaultPipelineSchedulerService;
import com.example.scheduler.service.PipelineSchedulerService;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.annotation.TransactionMode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.micronaut.transaction.interceptor.TransactionalInterceptor;
import jakarta.inject.Inject;
import org.javatuples.Quintet;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static io.micronaut.http.HttpResponse.ok;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@MicronautTest(
        environments = "it",
        propertySources = "file:src/it/resources/application-it.yml",
        transactional = false
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SchedulerControllerIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    private JobPipelineClient jobPipelineClient;

    @MockBean(JobPipelineClient.class)
    JobPipelineClient jobPipelineClient() {
        return mock(JobPipelineClient.class);
    }

    @Inject
    private PipelineSchedulerRepository pipelineSchedulerRepository;
    @Inject
    private JobPipelineSchedulerRepository jobPipelineSchedulerRepository;
    @Inject
    private InfiniteJob infiniteJob;

    @BeforeAll
    void beforeAll(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        jobPipelineSchedulerRepository.deleteAll();
        pipelineSchedulerRepository.deleteAll();

        infiniteJob.setActive(false);
    }

    @BeforeEach
    void beforeEach() {
        jobPipelineSchedulerRepository.deleteAll();
        pipelineSchedulerRepository.deleteAll();

        infiniteJob.setActive(false);
    }

    @Test
    @DisplayName("Should have running application")
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    @DisplayName("Should create scheduler")
    void testCreateScheduler() {
        // given
        var now = LocalDateTime.now();
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.INSTANT)
                .cron("*/1 * * * * ?") // every 1 second
                .active(true)
                .running(false)
                .deleted(false)
                .build();

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        // then
        scheduler.setId(result.getId());
        scheduler.setNextRunDate(result.getNextRunDate());
        scheduler.setCreatedDate(result.getCreatedDate());
        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getCreatedDate());
        Assertions.assertTrue(now.isBefore(result.getNextRunDate()));
        Assertions.assertTrue(now.isBefore(result.getCreatedDate()));
        Assertions.assertEquals(scheduler, result);
    }

    @Test
    @DisplayName("Should create instant scheduler and run job")
    void testCreateInstantSchedulerAndRunJob() {
        // given
        infiniteJob.setActive(true);
        var now = LocalDateTime.now(ZoneOffset.UTC);
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.INSTANT)
                .active(true)
                .deleted(false)
                .build();

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        // then
        scheduler.setId(result.getId());
        scheduler.setNextRunDate(result.getNextRunDate());
        scheduler.setCreatedDate(result.getCreatedDate());
        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getCreatedDate());
        Assertions.assertEquals(scheduler, result);
        Assertions.assertTrue(now.isBefore(result.getNextRunDate()));
        Assertions.assertTrue(now.isBefore(result.getCreatedDate()));
    }

    @Test
    @DisplayName("Should create cron scheduler and run job")
    void testCreateCronSchedulerAndRunJob() throws InterruptedException {
        // given
        infiniteJob.setActive(true);
        var jobIntervalMilliseconds = InfiniteJob.RATE_MILLISECONDS;
        var schedulerIntervalEvery1SecondInMilliseconds = 1000;
        var waitingIntervalMilliseconds = jobIntervalMilliseconds + schedulerIntervalEvery1SecondInMilliseconds;

        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.CRON)
                .cron("*/1 * * * * ?") // every 1 second
                .active(true)
                .running(false)
                .deleted(false)
                .build();

        // when
        var createdSchedulerResult = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        Thread.sleep(waitingIntervalMilliseconds);

        var allJobsResult = client.toBlocking().retrieve(
                HttpRequest.GET("/job/getAll"),
                JobPipelineSchedulerModel[].class);

        var allSchedulerResult = client.toBlocking().retrieve(
                HttpRequest.GET("/job/getAllBySchedulerId/" + createdSchedulerResult.getId()),
                JobPipelineSchedulerModel[].class);

        // then
        Assertions.assertEquals(1, allSchedulerResult.length);
        Assertions.assertArrayEquals(allSchedulerResult, allJobsResult);
        Assertions.assertTrue(allSchedulerResult[0].getStartDate().isAfter(createdSchedulerResult.getNextRunDate()));
    }

    @Test
    @DisplayName("Should get scheduler")
    void testGetScheduler() {
        // given
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.CRON)
                .cron("0/10 0 0 ? * * *")
                .active(true)
                .running(false)
                .nextRunDate(LocalDateTime.now())
                .deleted(false)
                .build();
        var createdScheduler = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.GET("/" + createdScheduler.getId()),
                PipelineSchedulerModel.class);

        // then
        Assertions.assertEquals(createdScheduler, result);
    }

    @Test
    @DisplayName("Should get all schedulers")
    void testGetAllSchedulers() {
        // given
        var currentDateTime = LocalDateTime.now();

        var examples = List.of(
                // pipelineId | nextRunDate | schedulerType | active | running | deleted
                Sextet.with(1L, currentDateTime.minusMinutes(5), SchedulerType.CRON, true, false, false),
                Sextet.with(2L, currentDateTime.minusMinutes(15), SchedulerType.INSTANT, true, false, true),
                Sextet.with(3L, currentDateTime.minusMinutes(25), SchedulerType.CALENDAR, true, false, false)
        );

        examples.stream()
                .map(s -> buildScheduler(s.getValue0(), s.getValue1(), s.getValue2(), null, s.getValue3(), s.getValue4(), s.getValue5()))
                .map(s -> pipelineSchedulerRepository.saveAndFlush(s))
                .collect(Collectors.toList());

        var expectedSchedulers = pipelineSchedulerRepository.findAll().stream()
                .map(PipelineSchedulerMapper.MAPPER::toModel)
                .collect(Collectors.toList());

        // when
        var result = Arrays.asList(client.toBlocking().retrieve(
                HttpRequest.GET("/"),
                PipelineSchedulerModel[].class));

        // then
        Assertions.assertEquals(expectedSchedulers, result);
    }

    @Test
    @DisplayName("Should update existing scheduler")
    void testUpdateScheduler() {
        // given
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.CRON)
                .cron("0/10 0 0 ? * * *")
                .active(true)
                .running(false)
                .nextRunDate(LocalDateTime.now())
                .deleted(false)
                .build();
        var createdScheduler = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        createdScheduler.setCron("0/5 0 0 ? * * *");
        createdScheduler.setActive(false);

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.PUT("/", createdScheduler),
                PipelineSchedulerModel.class);

        // then
        Assertions.assertEquals(createdScheduler, result);
    }

    @Test
    @DisplayName("Should delete scheduler with setting flag to deleted")
    void testUDeleteScheduler() {
        // given
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.CRON)
                .cron("0/10 0 0 ? * * *")
                .active(true)
                .running(false)
                .nextRunDate(LocalDateTime.now())
                .deleted(false)
                .build();
        var createdScheduler = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.DELETE("/" + createdScheduler.getId()),
                PipelineSchedulerModel.class);

        // then
        createdScheduler.setDeleted(true);
        Assertions.assertEquals(createdScheduler, result);
    }

    @Test
    @DisplayName("Should force to create and run instant scheduler")
    void testForceRun() {
        // given
        var pipelineId = 1L;
        var currentDateTime = LocalDateTime.now();

        when(jobPipelineClient.start(not(eq(pipelineId)), anyLong(), anyLong()))
                .thenThrow(new RuntimeException("Incorrect pipeline ID passed to the client."));

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.GET("/force/run/" + pipelineId),
                PipelineSchedulerModel.class);

        // then
        var job = result.getRuns().get(0);
        Assertions.assertTrue(currentDateTime.isBefore(result.getNextRunDate()));
        Assertions.assertEquals(pipelineId, result.getPipelineId());
        Assertions.assertEquals(SchedulerTypeModel.INSTANT, result.getSchedulerType());
        Assertions.assertTrue(result.isActive());
        Assertions.assertTrue(result.isRunning());
        Assertions.assertTrue(result.getRuns().size() == 1);
        Assertions.assertEquals(pipelineId, job.getPipelineId());
        verify(jobPipelineClient, times(1))
                .start(pipelineId, result.getId(), job.getId());
    }

    @Test
    @DisplayName("Should force to run first active scheduler with the oldest next run date")
    void testForceRunFirst() {
        // given
        var currentDateTime = LocalDateTime.now();

        var examples = List.of(
                // pipelineId | nextRunDate | schedulerType | active | running
                Quintet.with(1L, currentDateTime.minusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(2L, currentDateTime.minusMinutes(15), SchedulerType.INSTANT, true, false),
                Quintet.with(3L, currentDateTime.minusMinutes(25), SchedulerType.CALENDAR, true, false),

                Quintet.with(4L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, true),
                Quintet.with(5L, currentDateTime.minusMinutes(35), SchedulerType.CRON, false, false),
                Quintet.with(6L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, true, true),
                Quintet.with(7L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, false, false),
                Quintet.with(8L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, false),

                Quintet.with(9L, currentDateTime.minusMinutes(1), SchedulerType.CRON, true, false),
                Quintet.with(10L, currentDateTime.plusMinutes(1), SchedulerType.INSTANT, true, false),
                Quintet.with(11L, currentDateTime.plusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(12L, currentDateTime.plusMinutes(5), SchedulerType.CALENDAR, true, false)
        );

        var schedulerDomains = examples.stream()
                .map(s -> buildScheduler(s.getValue0(),s.getValue1(),s.getValue2(), "", s.getValue3(), s.getValue4(), false))
                .collect(Collectors.toList());

        var createdSchedulerDomains = schedulerDomains.stream()
                .map(s -> pipelineSchedulerRepository.saveAndFlush(s))
                .collect(Collectors.toList());

        var expectedSchedulerDomain = createdSchedulerDomains.stream().filter(p -> p.getPipelineId() == 8).findFirst().get();
        var expectedScheduler = client.toBlocking().retrieve(
                HttpRequest.GET("/" + expectedSchedulerDomain.getId()),
                PipelineSchedulerModel.class);
        expectedScheduler.setRunning(true);
        var expectedId = (long) expectedScheduler.getId();
        var expectedPipelineId = (long) expectedScheduler.getPipelineId();

        when(jobPipelineClient.start(not(eq(expectedPipelineId)), not(eq(expectedId)), anyLong()))
                .thenThrow(new RuntimeException("Incorrect pipeline ID and/or scheduler ID passed to the client."));

        // when
        var result = client.toBlocking().retrieve(
                HttpRequest.GET("/force/runFirst"),
                PipelineSchedulerModel.class);

        // then
        var job = result.getRuns().get(0);
        Assertions.assertEquals(1, result.getRuns().size());
        Assertions.assertTrue(currentDateTime.isBefore(job.getStartDate()));
        Assertions.assertNull(job.getEndDate());
        result.setRuns(new ArrayList<>());
        Assertions.assertEquals(expectedScheduler, result);
        verify(jobPipelineClient, times(1))
                .start(expectedPipelineId, result.getId(), job.getId());
    }

    @Test
    @DisplayName("Should force to run all active schedulers")
    void testForceRunAll() {
        // given
        var currentDateTime = LocalDateTime.now();

        var examples = List.of(
                // pipelineId | nextRunDate | schedulerType | active | running
                Quintet.with(1L, currentDateTime, SchedulerType.CRON, true, false),
                Quintet.with(1L, currentDateTime.minusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(2L, currentDateTime.minusMinutes(15), SchedulerType.INSTANT, true, false),
                Quintet.with(3L, currentDateTime.minusMinutes(25), SchedulerType.CALENDAR, true, false),

                Quintet.with(4L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, true),
                Quintet.with(5L, currentDateTime.minusMinutes(35), SchedulerType.CRON, false, false),
                Quintet.with(6L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, true, true),
                Quintet.with(7L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, false, false),
                Quintet.with(8L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, false),
                Quintet.with(9L, currentDateTime.minusMinutes(1), SchedulerType.CRON, true, false),

                Quintet.with(10L, currentDateTime.plusMinutes(1), SchedulerType.INSTANT, true, false),
                Quintet.with(11L, currentDateTime.plusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(12L, currentDateTime.plusMinutes(5), SchedulerType.CALENDAR, true, false)
        );

        var schedulerDomains = examples.stream()
                .map(s -> buildScheduler(s.getValue0(), s.getValue1(), s.getValue2(), "", s.getValue3(), s.getValue4(), false))
                .collect(Collectors.toList());

        var createdSchedulerDomains = schedulerDomains.stream()
                .map(s -> pipelineSchedulerRepository.saveAndFlush(s))
                .collect(Collectors.toList());

        var expectedSchedulerDomains = createdSchedulerDomains.stream()
                .filter(p -> LocalDateTime.now().isAfter(p.getNextRunDate())
                        && !p.isRunning()
                        && p.isActive())
                .sorted(Comparator.comparing(Scheduler::getNextRunDate))
                .collect(Collectors.toList());

        var expectedSchedulers = expectedSchedulerDomains.stream()
                .map(s -> client.toBlocking().retrieve(
                        HttpRequest.GET("/" + s.getId()),
                        PipelineSchedulerModel.class))
                .collect(Collectors.toList());
        expectedSchedulers.forEach(p -> p.setRunning(true));

        // when
        var result = Arrays.stream(client.toBlocking().retrieve(
                HttpRequest.GET("/force/runAll"),
                PipelineSchedulerModel[].class))
                .collect(Collectors.toList());

        // then
        var actualSchedulers = result.stream()
                .map(PipelineSchedulerModel::toBuilder)
                .map(p -> p.runs(new ArrayList<>()))
                .map(PipelineSchedulerModel.PipelineSchedulerModelBuilder::build)
                .collect(Collectors.toList());

        Assertions.assertArrayEquals(expectedSchedulers.toArray(), actualSchedulers.toArray());
        result.forEach(s -> {
            Assertions.assertEquals(1, s.getRuns().size());
            Assertions.assertTrue(s.getRuns().get(0).getStartDate().isAfter(currentDateTime));
            Assertions.assertEquals(s.getRuns().get(0).getPipelineId(), s.getPipelineId());
            Assertions.assertNull(s.getRuns().get(0).getEndDate());

            verify(jobPipelineClient, times(1))
                    .start(s.getPipelineId(), s.getId(), s.getRuns().get(0).getId());
        });

        verify(jobPipelineClient, times(expectedSchedulers.size()))
                .start(anyLong(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Should force to run asynchronously all active schedulers")
    void testForceRunAllAsync() {
        // given
        var cores = 2;
        var currentDateTime = LocalDateTime.now();

        var examples = List.of(
                // pipelineId | nextRunDate | schedulerType | active | running
                Quintet.with(1L, currentDateTime, SchedulerType.CRON, true, false),
                Quintet.with(1L, currentDateTime.minusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(2L, currentDateTime.minusMinutes(15), SchedulerType.INSTANT, true, false),
                Quintet.with(3L, currentDateTime.minusMinutes(25), SchedulerType.CALENDAR, true, false),

                Quintet.with(4L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, true),
                Quintet.with(5L, currentDateTime.minusMinutes(35), SchedulerType.CRON, false, false),
                Quintet.with(6L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, true, true),
                Quintet.with(7L, currentDateTime.minusMinutes(35), SchedulerType.INSTANT, false, false),
                Quintet.with(8L, currentDateTime.minusMinutes(35), SchedulerType.CRON, true, false),
                Quintet.with(9L, currentDateTime.minusMinutes(1), SchedulerType.CRON, true, false),

                Quintet.with(10L, currentDateTime.plusMinutes(1), SchedulerType.INSTANT, true, false),
                Quintet.with(11L, currentDateTime.plusMinutes(5), SchedulerType.CRON, true, false),
                Quintet.with(12L, currentDateTime.plusMinutes(5), SchedulerType.CALENDAR, true, false)
        );

        var schedulerDomains = examples.stream()
                .map(s -> buildScheduler(s.getValue0(), s.getValue1(), s.getValue2(), "", s.getValue3(), s.getValue4(), false))
                .collect(Collectors.toList());

        var createdSchedulerDomains = schedulerDomains.stream()
                .map(s -> pipelineSchedulerRepository.saveAndFlush(s))
                .collect(Collectors.toList());

        var expectedSchedulerDomains = pipelineSchedulerRepository.findAll().stream()
                .filter(p -> LocalDateTime.now().isAfter(p.getNextRunDate())
                        && !p.isRunning()
                        && p.isActive())
                .sorted(Comparator.comparing(Scheduler::getNextRunDate))
                .collect(Collectors.toList());

        var expectedSchedulers = expectedSchedulerDomains.stream()
                .map(s -> client.toBlocking().retrieve(
                        HttpRequest.GET("/" + s.getId()),
                        PipelineSchedulerModel.class))
                .collect(Collectors.toList());
        expectedSchedulers.forEach(p -> p.setRunning(true));

        // when
        var result = Arrays.stream(client.toBlocking().retrieve(
                HttpRequest.GET("/force/runAllAsync/" + cores),
                PipelineSchedulerModel[].class))
                .collect(Collectors.toList());

        // then
        var actualSchedulers = result.stream()
                .map(PipelineSchedulerModel::toBuilder)
                .map(p -> p.runs(new ArrayList<>()))
                .map(PipelineSchedulerModel.PipelineSchedulerModelBuilder::build)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedSchedulers.size(), actualSchedulers.size());
        Assertions.assertTrue(expectedSchedulers.containsAll(actualSchedulers));
        Assertions.assertTrue(actualSchedulers.containsAll(expectedSchedulers));

        result.forEach(s -> {
            Assertions.assertEquals(1, s.getRuns().size());
            Assertions.assertTrue(s.getRuns().get(0).getStartDate().isAfter(currentDateTime));
            Assertions.assertEquals(s.getRuns().get(0).getPipelineId(), s.getPipelineId());
            Assertions.assertNull(s.getRuns().get(0).getEndDate());

            verify(jobPipelineClient, times(1))
                    .start(s.getPipelineId(), s.getId(), s.getRuns().get(0).getId());
        });

        verify(jobPipelineClient, times(expectedSchedulerDomains.size()))
                .start(anyLong(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Should complete cron scheduler job")
    void testCompleteCronJob() throws InterruptedException {
        // given
        var now = LocalDateTime.now();
        var cronSecondInMilliseconds = 1000;
        var scheduler = PipelineSchedulerModel.builder()
                .pipelineId(1L)
                .schedulerType(SchedulerTypeModel.CRON)
                .cron("*/1 * * * * ?") // every 1 second
                .active(true)
                .running(false)
                .deleted(false)
                .build();

        // when
        var createdSchedulerResult = client.toBlocking().retrieve(
                HttpRequest.POST("/", scheduler),
                PipelineSchedulerModel.class);

        Thread.sleep(cronSecondInMilliseconds);

        var runningSchedulerResult = client.toBlocking().retrieve(
                HttpRequest.GET("/force/runFirst"),
                PipelineSchedulerModel.class);

        var completedJobResult = client.toBlocking().retrieve(
                HttpRequest.POST("/complete/" + createdSchedulerResult.getId(), null),
                PipelineSchedulerModel.class);

        // then
        var jobActual = completedJobResult.getRuns().get(0);
        var jobExpected = runningSchedulerResult.getRuns().get(0);

        Assertions.assertEquals(1, completedJobResult.getRuns().size());
        Assertions.assertEquals(jobExpected.getId(), jobActual.getId());
        Assertions.assertEquals(scheduler.getPipelineId(), jobActual.getPipelineId());
        Assertions.assertEquals(createdSchedulerResult.getId(), jobActual.getSchedulerId());
        Assertions.assertEquals(createdSchedulerResult.getId(), completedJobResult.getId());
        Assertions.assertTrue(jobActual.getStartDate().isBefore(jobActual.getEndDate()));
        Assertions.assertTrue(now.isBefore(createdSchedulerResult.getCreatedDate()));
    }

    private Scheduler buildScheduler(long pipelineId, LocalDateTime nextRunDate, SchedulerType schedulerType, String cron, boolean active, boolean running, boolean deleted){
        var result = Scheduler.builder()
                        .pipelineId(pipelineId)
                        .nextRunDate(nextRunDate)
                        .schedulerType(schedulerType)
                        .cron(cron)
                        .active(active)
                        .running(running)
                        .deleted(false)
                        .build();
        return result;
    }
}
