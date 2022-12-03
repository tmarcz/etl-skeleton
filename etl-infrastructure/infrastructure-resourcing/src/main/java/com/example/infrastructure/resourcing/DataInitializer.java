package com.example.infrastructure.resourcing;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Requires(notEnv = "mock")
@Slf4j
public class DataInitializer implements ApplicationEventListener<ServerStartupEvent> {
//    @Inject
//    private PostRepository postRepository;
//    @Inject
//    private TransactionOperations<?> tx;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
//        log.info("initializing sample data...");
//
//        var data = List.of(
//                Post.builder().title("Demo 01").content("Content 01").build(),
//                Post.builder().title("Demo 02").content("Content 02").build(),
//                Post.builder().title("Demo 03").content("Content 03").build(),
//                Post.builder().title("Demo 04").content("Content 04").build()
//        );
//        tx.executeWrite(status -> {
//            this.postRepository.deleteAll();
//            this.postRepository.saveAll(data);
//            return null;
//        });
//        tx.executeRead(status -> {
//            this.postRepository.findAll().forEach(p -> log.info("saved post: {}", p));
//            return null;
//        });
//
//        log.info("data initialization is done...");
    }
}
