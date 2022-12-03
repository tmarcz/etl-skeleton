package com.example.infrastructure.resourcing.repository;

import com.example.infrastructure.resourcing.domain.Resource;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface ResourceRepository  extends JpaRepository<Resource, Long> {
    @Executable
    Resource findByTitle(String title);
}
