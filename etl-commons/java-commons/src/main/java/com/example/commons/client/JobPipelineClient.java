package com.example.commons.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;

@Client
public interface JobPipelineClient {

    // TODO: what information do we need to send ?
    // TODO: what information do we expect to receive ?

    @Get(uri = "http://localhost:9610/job/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> sendStatus(@PathVariable long id);

    @Get(uri = "http://localhost:9610/ping", produces = MediaType.TEXT_PLAIN)
    @Header(name = "Accept", value = "text/plain")
    HttpResponse<String> ping();

}