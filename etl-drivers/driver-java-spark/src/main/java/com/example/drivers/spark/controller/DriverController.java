package com.example.drivers.spark.controller;

import com.example.drivers.spark.client.PipelineClient;
import com.example.drivers.spark.model.ApplicationSuiteModel;
import com.example.drivers.spark.service.PipelineExecutionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static io.micronaut.http.HttpResponse.ok;

@Generated(value="org.openapitools.codegen.languages.JavaMicronautServerCodegen")
@Controller
@Tag(name = "Driver", description = "The Driver API")
public class DriverController {

    @Inject
    private PipelineExecutionService pipelineExecutionService;
    @Inject
    private PipelineClient pipelineClient;

//    public DriverController(PipelineExecutionService pipelineExecutionService, PipelineClient pipelineClient) {
//        this.pipelineExecutionService = pipelineExecutionService;
//        this.pipelineClient = pipelineClient;
//    }

    /**
     * ping
     *
     */
    @Operation(
            operationId = "ping",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @Get(uri="/ping")
    @Produces(value = MediaType.APPLICATION_JSON)
    @Secured({SecurityRule.IS_ANONYMOUS})
    public HttpResponse<String> ping() {

        var test = pipelineExecutionService.run(null);

        return ok("pong!" + test);
    }

    /**
     * Run application suite
     *
     * @param applicationSuiteModel  (required)
     */
    @Operation(
            operationId = "run",
            summary = "Run application suite",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            },
            parameters = {
                    @Parameter(name = "applicationSuiteModel", required = true)
            }
    )
    @Post(uri="/run")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(value = {"application/json"})
    @Secured({SecurityRule.IS_ANONYMOUS})
    public HttpResponse<String> run(
            @Body @NotNull @Valid ApplicationSuiteModel applicationSuite
    ) {
        var test = pipelineExecutionService.run(applicationSuite);
        return ok("RESULT =" + applicationSuite.getPipelineId());
    }

}
