package com.example.workers.activities.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EngineClient {

    public static String ping() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:9610/job/ping"))
                .header("accept", "text/plain")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = response.body();
        return result;
    }
}