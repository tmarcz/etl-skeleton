package com.example.infrastructure.resourcing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResourceModel {
    private Long id ;
    private String title;
    private String content;
}
