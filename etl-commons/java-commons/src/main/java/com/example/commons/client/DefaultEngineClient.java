package com.example.commons.client;

import com.example.commons.model.ApplicationSuiteModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultEngineClient {
    public static ApplicationSuiteModel get(long pipelineId) {
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9615/"+pipelineId))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var objectMapper = new ObjectMapper();
            var result = objectMapper.readValue(response.body(), ApplicationSuiteModel.class);
            return result;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
