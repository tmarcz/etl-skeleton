package com.example.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionStepPipelineModel {
    private Long pipelineId; // TODO: -> it is job ID
    private String group;
    private String title;
    private String message;
}
