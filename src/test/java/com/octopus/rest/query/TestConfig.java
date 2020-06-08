package com.octopus.rest.query;


import com.octopus.rest.query.service.SearchService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public SearchService<TestTableEntity> searchService(TestTableRepository testTableRepository) {
        return new SearchService<>(testTableRepository);
    }

}
