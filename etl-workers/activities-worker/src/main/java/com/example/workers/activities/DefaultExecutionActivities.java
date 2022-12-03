package com.example.workers.activities;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class DefaultExecutionActivities implements ExecutionActivities {

    @Override
    public String start(int id) {
        // TODO:
        // 1. get full pipelines details from repo | PipelineRepository
        // 2. get exec engine implementations | ExecutionEngineService
        // 3. get resource allocation | ResourceInfraService
        // 4. push app suite | DriverService
        // // move to gRPC for optimization

        System.out.println("1 start...");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8810/driver/ping/")).build();
        System.out.println(Instant.now());
        String ping = null;
        try {
            ping = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println(Instant.now());
        System.out.println("# ping: " + ping);

//        try {
//            Thread.sleep(10000);
//        } catch (Exception ignored) {
//        }

        System.out.println("2 start...");

        return "start: " + id;
    }

    @Override
    public String end(int id) {
        return null;
    }

    @Override
    public String terminate(int id) {
        return null;
    }

    @Override
    public String ping() {
        System.out.println("ping...");
        return "pong";
    }
}
