package com.fjss23.jobsearch.error;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

public sealed interface ErrorStructure {
    record ErrorField(String field, List<String> messages)
        implements ErrorStructure {}

    record ErrorApi(List<String> general, Map<String, List<String>> fields)
        implements ErrorStructure {}

    record ErrorResponse(
        HttpStatus status,
        String path,
        String message,
        ErrorApi errors
    )
        implements ErrorStructure {}
}
