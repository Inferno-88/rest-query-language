package com.octopus.rest.query;

import com.octopus.rest.query.repository.entity.SearchableEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
public class GenericSpecification<T extends SearchableEntity> implements Specification<T> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(getAttributePath(root), criteria.getValue());
            case NEGATION:
                return builder.notEqual(getAttributePath(root), criteria.getValue());
            case GREATER_THAN:
                return getGreaterThanOrEqualToPredicate(root, builder);
            case LESS_THAN:
                return getLessThanOrEqualToPredicate(root, builder);
            case STARTS_WITH:
                return builder.like(getAttributePath(root), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(getAttributePath(root), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(getAttributePath(root), "%" + criteria.getValue() + "%");
            case IN:
                return getInPredicate(root, builder);
            default:
                return null;
        }
    }

    private <X> Path<X> getAttributePath(Root<T> root) {
        if (isNestedKey()) {
            return getNestedAttributePath(root);
        } else {
            return root.get(criteria.getKey());
        }
    }

    private boolean isNestedKey() {
        return criteria.getKey().contains(".");
    }

    private <X> Path<X> getNestedAttributePath(Root<T> root) {
        List<String> keys = Arrays.asList(criteria.getKey().split("\\."));

        Path<X> path = root.get(keys.get(0));
        for (int i = 1; i < keys.size(); i++) {
            path = path.get(keys.get(i));
        }
        return path;
    }

    //GREATER, LESS
    private Predicate getGreaterThanOrEqualToPredicate(Root<T> root, CriteriaBuilder builder) {
        if (isDateKey(root)) {
            return builder.greaterThanOrEqualTo(getAttributePath(root), parseDate());
        } else {
            return builder.greaterThanOrEqualTo(getAttributePath(root), criteria.getValue().toString());
        }
    }

    private Predicate getLessThanOrEqualToPredicate(Root<T> root, CriteriaBuilder builder) {
        if (isDateKey(root)) {
            return builder.lessThanOrEqualTo(getAttributePath(root), parseDate());
        } else {
            return builder.lessThanOrEqualTo(getAttributePath(root), criteria.getValue().toString());
        }
    }

    private boolean isDateKey(Root<T> root) {
        return getAttributePath(root).getJavaType().getName().equals(OffsetDateTime.class.getName());
    }

    private ZonedDateTime parseDate() {
        return ZonedDateTime.parse(criteria.getValue().toString(), DATE_TIME_FORMATTER);
    }

    //IN
    private Predicate getInPredicate(Root<T> root, CriteriaBuilder builder) {
        CriteriaBuilder.In<Object> in = builder.in(getAttributePath(root));

        List<String> values = Arrays.asList(criteria.getValue().toString().split(","));
        if (values.stream().allMatch(StringUtils::isNumeric)) {
            values.stream()
                    .mapToInt(Integer::parseInt)
                    .forEach(in::value);
        } else {
            values.forEach(in::value);
        }
        return in;
    }

}
