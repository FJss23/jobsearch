package com.fjss23.jobsearch.auth;

import com.fjss23.jobsearch.auth.login.LoginRequestDto;
import com.fjss23.jobsearch.auth.registration.RegistrationRequestDto;
import com.fjss23.jobsearch.auth.registration.RegistrationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegistrationRequestDto request) {
        return registrationService.register(request);
    }

    @GetMapping("/registration/confirm")
    public void verifyRegistration(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginRequestDto login) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            login.email(), login.password()));
        //Cookie cookie = new Cookie()
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody String login) {
        logger.info("login out!");
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/forgot-password")
    public void forgotPassword() {
        // todo: implement the function
    }

    @PostMapping("/change-password")
    public void changePassword() {
        // todo: implement the function
    }
}
