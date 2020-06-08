package com.octopus.rest.query.repository;

import com.octopus.rest.query.repository.entity.SearchableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchableRepository<T extends SearchableEntity> extends JpaRepository<T, String>,
        JpaSpecificationExecutor<T> {
}
