package com.fjss23.jobsearch;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping("/api/v1/")
public @interface ApiV1PrefixController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
