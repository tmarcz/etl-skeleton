package com.example.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplicationSuiteModel {
    private Long pipelineId ;
    private Long jobId;

    private ResourceModel resource;
    private List<ApplicationStepPipelineModel> pipeline;
}
