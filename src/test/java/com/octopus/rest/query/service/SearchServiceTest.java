package com.octopus.rest.query.service;


import com.octopus.rest.query.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = {
        TestConfig.class
})
@ExtendWith(SpringExtension.class)
@Slf4j
public class SearchServiceTest {

    @MockBean
    private TestTableRepository testTableRepository;

    @Captor
    ArgumentCaptor<Specification<TestTableEntity>> specificationCaptor;

    @Autowired
    private SearchService<TestTableEntity> searchService;


    @SneakyThrows
    @Test
    public void shouldReturnSomeEntities_When_SearchQuerySpecified() {
        //Given
        String searchQuery = "someField:value1,value2";
        List<TestTableEntity> testTableEntityList = List.of(
                new TestTableEntity("2", "value1", false),
                new TestTableEntity("3", "value2", true)
        );

        //When
        when(testTableRepository.findAll(specificationCaptor.capture())).thenReturn(testTableEntityList);
        List<TestTableEntity> resultList = searchService.search(searchQuery).get();

        //Then
        assertEquals(((GenericSpecification<TestTableEntity>) specificationCaptor.getValue()).getCriteria(),
                new SearchCriteria("someField", SearchOperation.IN, "value1,value2"));
        assertThat(resultList, Matchers.hasItems(
                new TestTableEntity("2", "value1", false),
                new TestTableEntity("3", "value2", true)
        ));
    }

    @SneakyThrows
    @Test
    public void shouldReturnAllEntities_When_SearchQueryNotSpecified() {
        //Given
        String searchQuery = "";

        //When
        List<TestTableEntity> resultList = searchService.search(searchQuery).get();

        //Then
        verify(testTableRepository, times(1)).findAll();
    }

}
