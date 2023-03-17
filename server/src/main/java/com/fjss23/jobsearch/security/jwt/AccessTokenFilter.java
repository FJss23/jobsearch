package com.fjss23.jobsearch.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AccessTokenFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    public AccessTokenFilter(JwtHelper jwtHelper, RedisTemplate<String, String> redisTemplate) {
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Optional<String> accessToken = getAccessTokenFromHeader(request);
            if (accessToken.isPresent() && jwtHelper.verifyAccessToken(accessToken.get())) {

                String token = redisTemplate.opsForValue().get("token_" + accessToken.get());
                if (StringUtils.hasText(token)) throw new BadCredentialsException("Invalid token");

                DecodedJWT jwtToken = jwtHelper.getJwtAccessToken(accessToken.get());
                String username = jwtToken.getSubject();
                String role = jwtToken.getClaim(JwtHelper.ROLE).asString();

                var authority = new SimpleGrantedAuthority(role);
                var authorities = Collections.singletonList(authority);

                org.springframework.security.core.userdetails.User principal = new User(username, "", authorities);
                var upat = new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);

                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        } catch (Exception e) {
            logger.info("Something went wrong trying to authenticate the user from the token");
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }
        return Optional.empty();
    }
}
