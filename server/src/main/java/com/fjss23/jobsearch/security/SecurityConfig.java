package com.fjss23.jobsearch.security;

import com.fjss23.jobsearch.security.jwt.AccessTokenEntryPoint;
import com.fjss23.jobsearch.security.jwt.AccessTokenFilter;
import com.fjss23.jobsearch.security.jwt.JwtHelper;
import com.fjss23.jobsearch.user.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Some interesting resources:
 * - https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.websecurity.debug:false}")
    boolean webSecurityDebug;

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccessTokenEntryPoint accessTokenEntryPoint;
    private final JwtHelper jwtHelper;
    private final RedisTemplate<String, String> redisTemplate;

    public SecurityConfig(
            AppUserService appUserService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AccessTokenEntryPoint accessTokenEntryPoint,
            JwtHelper jwtHelper,
            RedisTemplate<String, String> redisTemplate) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessTokenEntryPoint = accessTokenEntryPoint;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * If this is set to True, client-side JavaScript will not be able to access the CSRF cookie.
     *
     * Designating the CSRF cookie as HttpOnly doesn't offer any practical protection because CSRF
     * is only to protect against cross-domain attacks. If an attacker can read the cookie via
     * JavaScript, they’re already on the same domain as far as the browser knows, so they can
     * do anything they like anyway. (XSS is a much bigger hole than CSRF.)
     *
     * Spring provides an out-of-the-box solution to exclude OPTIONS requests from authorization checks.
     * The cors() method will add the Spring-provided CorsFilter to the application context,
     * bypassing the authorization checks for OPTIONS requests
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .exceptionHandling(exception -> exception.authenticationEntryPoint(this.accessTokenEntryPoint))
                .authorizeHttpRequests(requests -> requests.requestMatchers("/api/v*/auth/login")
                        .permitAll()
                        .requestMatchers("/api/v*/auth/registration/**")
                        .permitAll()
                        .requestMatchers("/api/v*/auth/access-token")
                        .permitAll()
                        .requestMatchers("/api/v*/jobs")
                        .permitAll()
                        .requestMatchers("/api/v*/email/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/api/v*/auth/registration/**",
                                "/api/v*/auth/login",
                                "/api/v*/auth/logout",
                                "/api/v*/auth/access-token",
                                "/api/v*/email/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.bCryptPasswordEncoder);
        provider.setUserDetailsService(this.appUserService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter() {
        return new AccessTokenFilter(this.jwtHelper, this.redisTemplate);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(webSecurityDebug);
    }
}
