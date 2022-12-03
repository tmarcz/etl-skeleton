package com.example.drivers.spark.client;

import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

@Client(value = "http://localhost:8080/posts")
@Header(name = "Content-Type", value = "application/json")
@Header(name = "Accept", value = "application/json")
public interface PipelineClient {
}
