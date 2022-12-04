package com.example.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplicationStepPipelineModel {
    private Long id ;
    private String name;
    private String url;
    private String classPath;
    private String methodName;

    private Map<String, String> configuration;
}
