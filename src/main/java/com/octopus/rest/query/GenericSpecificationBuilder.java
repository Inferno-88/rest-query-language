package com.octopus.rest.query;

import com.octopus.rest.query.repository.entity.SearchableEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

public class GenericSpecificationBuilder<T extends SearchableEntity> {

    private final List<SearchCriteria> params = new ArrayList<>();

    public static <T extends SearchableEntity> GenericSpecificationBuilder<T> builder() {
        return new GenericSpecificationBuilder<>();
    }

    public void with(String key, String operation, String value, String prefix, String suffix) {
        SearchOperation op = SearchOperation.fromValue(operation);
        if (op != null) {
            op = determineOperation(op, prefix, suffix, value);
            params.add(new SearchCriteria(key, op, value));
        }
    }

    private SearchOperation determineOperation(SearchOperation op, String prefix, String suffix, String value) {
        if (op == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = prefix != null && prefix.contains("*");
            boolean endWithAsterisk = suffix != null && suffix.contains("*");
            boolean containsComma = value != null && value.contains(",");

            if (startWithAsterisk && endWithAsterisk) {
                op = SearchOperation.CONTAINS;
            } else if (startWithAsterisk) {
                op = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                op = SearchOperation.STARTS_WITH;
            } else if (containsComma) {
                op = SearchOperation.IN;
            }
        }
        return op;
    }

    public Optional<Specification<T>> build() {
        if (isEmpty(params)) {
            return Optional.empty();
        }

        Optional<Specification<T>> specification = Optional.of(new GenericSpecification<>(params.get(0)));
        for (int i = 1; i < params.size(); i++) {
            specification = Optional.ofNullable(Specification.where(specification.get())
                    .and(new GenericSpecification<>(params.get(i))));
        }
        return specification;
    }

}
