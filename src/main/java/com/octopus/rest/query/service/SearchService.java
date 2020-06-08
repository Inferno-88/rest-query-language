package com.octopus.rest.query.service;

import com.octopus.rest.query.GenericSpecificationBuilder;
import com.octopus.rest.query.SearchUtils;
import com.octopus.rest.query.repository.SearchableRepository;
import com.octopus.rest.query.repository.entity.SearchableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

@RequiredArgsConstructor
@Slf4j
public class SearchService<T extends SearchableEntity> {

    private final SearchableRepository<T> searchableRepository;

    public SearchableRepository<T> getSearchableRepository() {
        return searchableRepository;
    }

    public CompletableFuture<List<T>> search(String searchQuery) {
        Optional<Specification<T>> specification = getSpecification(searchQuery);
        return specification.map(spec -> CompletableFuture.supplyAsync(() -> findBySpecification(spec)))
                .orElseGet(() -> CompletableFuture.supplyAsync(searchableRepository::findAll));
    }

    private Optional<Specification<T>> getSpecification(String searchQuery) {
        GenericSpecificationBuilder<T> builder = GenericSpecificationBuilder.builder();
        Matcher matcher = SearchUtils.getMatcher(searchQuery);
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5)
            );
        }
        return builder.build();
    }

    private List<T> findBySpecification(Specification<T> spec) {
        return searchableRepository.findAll(spec);
    }

}
