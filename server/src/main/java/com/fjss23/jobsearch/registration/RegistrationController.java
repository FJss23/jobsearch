package com.fjss23.jobsearch.registration;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/registration")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(
        RegistrationController.class
    );

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegistrationRequestDto request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String verifyRegistration(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
