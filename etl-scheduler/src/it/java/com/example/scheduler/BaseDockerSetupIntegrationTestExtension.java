package com.example.scheduler;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class BaseDockerSetupIntegrationTestExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    /** Gate keeper to prevent multiple Threads within the same routine */
    private static final Lock LOCK = new ReentrantLock();
    /** volatile boolean to tell other threads, when unblocked, whether they should try attempt start-up.  Alternatively, could use AtomicBoolean. */
    private static volatile boolean started = false;

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        // lock the access so only one Thread has access to it
        LOCK.lock();
        try {
            if (!started) {
                started = true;
                String uniqueKey = this.getClass().getName();
                // Your "before all tests" startup logic goes here
                // The following line registers a callback hook when the root test context is
                // shut down
                context.getRoot().getStore(GLOBAL).put(uniqueKey, this);

                // do your work - which might take some time -
                // or just uses more time than the simple check of a boolean

//                GenericContainer nginxWithHttpWait = new GenericContainer(DockerImageName.parse("nginx:1.9.4"))
//                        .withExposedPorts(80)
//                        .with
//                        .waitingFor(Wait.forHttp("/"))
            }
        } finally {
            // free the access
            LOCK.unlock();
        }
    }

    @Override
    public void close() {
        // Your "after all tests" logic goes here
    }
}