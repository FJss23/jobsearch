package com.fjss23.jobsearch.auth;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.auth.login.LoginService;
import com.fjss23.jobsearch.auth.login.payload.LoginRequest;
import com.fjss23.jobsearch.auth.login.payload.LoginResponse;
import com.fjss23.jobsearch.auth.registration.RegistrationService;
import com.fjss23.jobsearch.auth.registration.payload.RegistrationRequest;
import com.fjss23.jobsearch.user.AppUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/*
 * Here we are doing a few things manually, mainly /logout and /login routes (instead of using spring security)
 * More info for manual logout: https://www.baeldung.com/spring-security-manual-logout
 */
@ApiV1PrefixController
public class AuthController {

    static final String REFRESH_TOKEN_COOKIE = "_rfsh";

    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;
    private final LoginService loginService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(
            AuthenticationManager authenticationManager,
            RegistrationService registrationService,
            LoginService loginService) {
        this.authenticationManager = authenticationManager;
        this.registrationService = registrationService;
        this.loginService = loginService;
    }

    @PostMapping("/auth/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest);
    }

    @GetMapping("/auth/registration/confirm")
    public void verifyRegistration(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }

    /*
     * Two JWT tokens are created, the [1] access token and the [2] refresh token. The former is sent as json
     * in the body of the response, the latter is sent in secured cookie.
     * First, we generate the [2] refresh token, the location, device and email of the authenticated user,
     * are stored in the database, an auto-generated id is created, this id is inserted into the claims of the
     * [2] token.
     * Lastly, the access token is generated, its claims includes the first name and the location of the user, also,
     * the id of the [2] refresh token is incorporated in the token.
     */
    @PostMapping("/auth/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
        var upat = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        Authentication authentication = authenticationManager.authenticate(upat);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        var appUser = (AppUser) securityContext.getAuthentication().getPrincipal();
        var location = request.getLocale().getCountry();
        var device = request.getHeader("User-Agent");

        LoginService.GenerateRefreshToken info = loginService.generateRefreshToken(appUser, location, device);
        String accessToken = loginService.generateAccessToken(appUser, info.id());

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, info.token());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new LoginResponse(accessToken);
    }

    /*
     * The following steps are involved in the logout process:
     *
     * 1. Obtain the [1] access token from the appropiate header, decode the token, read the expiration date
     * and add it to the redis database for blocked tokens, with the same expiration as its expiration date.
     *
     * 2. Obtain the id of the [2] refresh token from the [1] access token, delete the entry associated with
     * the [2] refresh token in the database. Add the [2] refresh token in the blocked list (following the same
     * method for expiration, described in the previous point).
     *
     * 3. Remove the [2] refresh token from the cookie.
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        loginService.removeAccessAndRefreshToken(accessToken);

        var deleteCookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/logout-all")
    public ResponseEntity<?> logoutAll(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        loginService.removeAllTokens(accessToken);

        var deleteCookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        return ResponseEntity.ok().build();
    }

    /*
     * We must check if the refresh token is the database, if not, the token was revoked.
     */
    @PostMapping("/auth/access-token")
    public ResponseEntity<?> getAccessToken(HttpServletRequest request) {
        Optional refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
                .findFirst();

        if (refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("No refresh token provided");
        }

        String token = ((Cookie) refreshToken.get()).getValue();
        if (!loginService.checkRefreshTokenValidity(token)) {
            return ResponseEntity.badRequest().body("The refresh token is not valid");
        }

        String newAccessToken = loginService.getNewAccessToken(token);
        return ResponseEntity.ok(newAccessToken);
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
