package com.octopus.rest.query;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class SearchUtils {

    public final String SEARCH_STRING = "([A-Za-z0-9_.]+?)(%)(\\*?)([A-Za-z0-9+-,]+?)(\\*?);";
    public final Pattern SEARCH_PATTERN;

    static {
        String operations = String.join("|", SearchOperation.SIMPLE_OPERATION_LIST);
        SEARCH_PATTERN = Pattern.compile(SEARCH_STRING.replace("%", operations));
    }

    public Matcher getMatcher(String searchQuery) {
        return SEARCH_PATTERN.matcher(searchQuery + ";");
    }

}
