package com.octopus.rest.query;

import com.octopus.rest.query.repository.SearchableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTableRepository extends SearchableRepository<TestTableEntity> {

}
