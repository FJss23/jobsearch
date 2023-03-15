package com.fjss23.jobsearch.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    static final String ISSUER = "JobSearch";
    static final String ROLE = "scope";
    static final String FIRST_NAME = "name";
    public static final String REFRESH_ID = "_rid";

    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;
    private Algorithm accessTokenAlgorithm;
    private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    private JWTVerifier refreshTokenVerifier;

    public JwtHelper(
        @Value("${app.jwt.access.secret}") String accessTokenSecret,
        @Value("${app.jwt.refresh.secret}") String refreshTokenSecret,
        @Value("${app.jwt.access.expiration.minutes}") int accessTokenExpirationMs,
        @Value("${app.jwt.refresh.expiration.days}") int refreshTokenExpirationMs

    ) {
        this.accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        this.refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        this.accessTokenVerifier = JWT.require(this.accessTokenAlgorithm).withIssuer(ISSUER).build();
        this.refreshTokenVerifier = JWT.require(this.refreshTokenAlgorithm).withIssuer(ISSUER).build();

        this.accessTokenExpirationMs = (long) accessTokenExpirationMs * 60 * 1000; // from minutes to milliseconds
        this.refreshTokenExpirationMs = (long) refreshTokenExpirationMs * 24 * 60 * 60 * 1000; // from days to milliseconds
    }

    public String generateAccessToken(String username, String role, String firstName, Long refreshId) {
        return JWT
            .create()
            .withIssuer(ISSUER)
            .withSubject(username)
            .withClaim(ROLE, role)
            .withClaim(FIRST_NAME, firstName)
            .withClaim(REFRESH_ID, refreshId)
            .withIssuedAt(new Date())
            .withExpiresAt(
                new Date(new Date().getTime() + this.accessTokenExpirationMs)
            )
            .sign(this.accessTokenAlgorithm);
    }

    public String generateRefreshToken(String username, long id) {
        return JWT
            .create()
            .withIssuer(ISSUER)
            .withSubject(username)
            .withClaim(REFRESH_ID, id)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(new Date().getTime() + this.refreshTokenExpirationMs))
            .sign(this.refreshTokenAlgorithm);
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            logger.info("Couldn't verify the access token");
            return Optional.empty();
        }
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            logger.info("Couldn't verify the refresh token");
            return Optional.empty();
        }
    }

    public boolean verifyAccessToken(String accessToken) {
        return decodeAccessToken(accessToken).isPresent();
    }

    public boolean verifyRefreshToken(String refreshToken) {
        return decodeRefreshToken(refreshToken).isPresent();
    }

    public String getUsernameFromAccessToken(String accessToken) {
        return decodeAccessToken(accessToken).get().getSubject();
    }

    public String getClaimValueFromAccessToken(String accessToken, String claim) {
        return decodeAccessToken(accessToken).get().getClaim(claim).asString();
    }

    public Long getIdFromAccessToken(String accessToken) {
        return decodeAccessToken(accessToken).get().getClaim(REFRESH_ID).asLong();
    }
}
