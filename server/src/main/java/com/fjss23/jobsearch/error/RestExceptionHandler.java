package com.fjss23.jobsearch.error;

import com.fjss23.jobsearch.error.ErrorStructure.*;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * More information and examples:
 * - https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(
        ResponseEntityExceptionHandler.class
    );

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        List<String> globalErrors = ex
            .getBindingResult()
            .getGlobalErrors()
            .stream()
            .map(objectError -> objectError.getDefaultMessage())
            .collect(Collectors.toList());

        Map<String, List<String>> groupedFieldErrors = new HashMap<>();
        var errorFields = new ArrayList<ErrorField>();

        ex
            .getFieldErrors()
            .forEach(error -> {
                if (groupedFieldErrors.containsKey(error.getField())) {
                    groupedFieldErrors
                        .get(error.getField())
                        .add(error.getDefaultMessage());
                } else {
                    var defaultMessages = new ArrayList<String>();
                    defaultMessages.add(error.getDefaultMessage());
                    groupedFieldErrors.put(error.getField(), defaultMessages);
                }
            });

        groupedFieldErrors.forEach((field, errorMessages) ->
            errorFields.add(new ErrorField(field, errorMessages))
        );

        var path = ((ServletWebRequest) request).getRequest().getRequestURI();

        var errorApi = new ErrorApi(globalErrors, groupedFieldErrors);
        var errorResponse = new ErrorResponse(
            (HttpStatus) status,
            path,
            "Invalid arguments",
            errorApi
        );

        return handleExceptionInternal(
            ex,
            errorResponse,
            headers,
            status,
            request
        );
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        var path = ((ServletWebRequest) request).getRequest().getRequestURI();

        var errorApi = new ErrorApi(
            Arrays.asList("Error occurred"),
            new HashMap<>()
        );
        var errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            path,
            ex.getLocalizedMessage(),
            errorApi
        );

        return new ResponseEntity<Object>(
            errorResponse,
            new HttpHeaders(),
            errorResponse.status()
        );
    }
}
