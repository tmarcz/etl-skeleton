package com.example.engine.pipeline.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImplementationStepPipelineModel {
    private Long id ;
    private String title;
    private String content;
}
