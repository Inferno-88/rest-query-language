package com.octopus.rest.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum SearchOperation {

    EQUALITY(":"),
    NEGATION("!"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    STARTS_WITH(null),
    ENDS_WITH(null),
    CONTAINS(null),
    IN(null);

    private final String value;

    public static final List<String> SIMPLE_OPERATION_LIST = Arrays.stream(values())
            .map(SearchOperation::getValue)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    public static SearchOperation fromValue(String operation) {
        return Arrays.stream(values())
                .filter(v -> v.getValue().equalsIgnoreCase(operation))
                .findFirst()
                .orElse(null);
    }

}
