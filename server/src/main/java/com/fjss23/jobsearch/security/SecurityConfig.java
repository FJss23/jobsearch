package com.fjss23.jobsearch.security;

import com.fjss23.jobsearch.user.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.websecurity.debug:false}")
    boolean webSecurityDebug;

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(
        AppUserService appUserService,
        BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * If this is set to True, client-side JavaScript will not be able to access the CSRF cookie.
     *
     * Designating the CSRF cookie as HttpOnly doesn’t offer any practical protection because CSRF
     * is only to protect against cross-domain attacks. If an attacker can read the cookie via
     * JavaScript, they’re already on the same domain as far as the browser knows, so they can
     * do anything they like anyway. (XSS is a much bigger hole than CSRF.)
     *
     * Spring provides an out of the box solution to exclude OPTIONS requests from authorization checks.
     * The cors() method will add the Spring-provided CorsFilter to the application context,
     * bypassing the authorization checks for OPTIONS requests
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
            .and()
                .authorizeHttpRequests(requests ->
                    requests
                        .requestMatchers(
                           "/api/v*/registration/**",
                            "/api/v*/email/**",
                            "/api/v*/logout",
                            "/api/v*/login",
                            "/api/v*/jobs"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .csrf(csrfConfigurer ->
                    csrfConfigurer
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                            "/api/v*/registration/**",
                            "/api/v*/email/**",
                            "/api/v*/logout",
                            "/api/v*/login"
                        )
                )
                .logout(logout ->
                    logout
                        .logoutUrl("/api/v*/logout")
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.bCryptPasswordEncoder);
        provider.setUserDetailsService(this.appUserService);
        return new ProviderManager(provider);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(webSecurityDebug);
    }
}
