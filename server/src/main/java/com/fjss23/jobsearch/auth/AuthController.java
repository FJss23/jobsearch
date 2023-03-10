package com.fjss23.jobsearch.auth;

import com.fjss23.jobsearch.auth.login.LoginRequestDto;
import com.fjss23.jobsearch.auth.registration.RegistrationRequestDto;
import com.fjss23.jobsearch.auth.registration.RegistrationService;
import com.fjss23.jobsearch.user.AppUser;
import com.fjss23.jobsearch.user.AppUserResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/*
 * Here we are doing a few things manually, mainly /logout and /login routes (instead of using spring security)
 * More info for manual logout: https://www.baeldung.com/spring-security-manual-logout
 */
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
    public AppUserResponseDto login(@Valid @RequestBody LoginRequestDto login, HttpServletRequest request) throws ServletException {
       logger.info("login in");
       request.login(login.email(), login.password());
       var auth = (Authentication) request.getUserPrincipal();
       var appUser = (AppUser) auth.getPrincipal();
       return new AppUserResponseDto(appUser.getFirstName(), appUser.getLastName(), appUser.getEmail(), appUser.getUserRole().name());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody String login, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.info("login out");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
        logger.info("Cookies {}", request.getCookies());
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String cookieName = cookie.getName();
                Cookie cookieToDelete = new Cookie(cookieName, null);
                cookieToDelete.setMaxAge(0);
                response.addCookie(cookieToDelete);
            }

        }
        Cookie sessionCookie = new Cookie("SESSION", null);
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
        logger.info("Session {}", request.getSession());
        ctxLogOut.setInvalidateHttpSession(true);
        ctxLogOut.setClearAuthentication(true);
        ctxLogOut.logout(request, response, auth);
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
