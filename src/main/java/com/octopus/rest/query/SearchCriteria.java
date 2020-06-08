package com.octopus.rest.query;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchCriteria {

    private final String key;
    private final SearchOperation operation;
    private final Object value;

}
