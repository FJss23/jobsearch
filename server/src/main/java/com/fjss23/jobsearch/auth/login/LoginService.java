package com.fjss23.jobsearch.auth.login;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fjss23.jobsearch.security.jwt.JwtHelper;
import com.fjss23.jobsearch.user.AppUser;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final JwtHelper jwtHelper;
    private final LoginRepository loginRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public LoginService(
            JwtHelper jwtHelper, LoginRepository loginRepository, RedisTemplate<String, String> redisTemplate) {
        this.jwtHelper = jwtHelper;
        this.loginRepository = loginRepository;
        this.redisTemplate = redisTemplate;
    }

    public RefreshTokenInfo generateRefreshToken(AppUser appUser, String location, String device) {
        RefreshAuth refreshAuth = new RefreshAuth(location, device, appUser.getEmail());
        Long id = loginRepository.create(refreshAuth);

        String token = jwtHelper.generateRefreshToken(
                appUser.getUsername(), appUser.getUserRole().name(), appUser.getFirstName(), id);
        return new RefreshTokenInfo(token, id);
    }

    public String generateAccessToken(AppUser appUser, Long refreshId) {
        return jwtHelper.generateAccessToken(
                appUser.getUsername(), appUser.getUserRole().name(), appUser.getFirstName(), refreshId);
    }

    public void removeAccessAndRefreshToken(String accessToken) {
        Long refreshId = jwtHelper
                .getJwtAccessToken(accessToken)
                .getClaim(JwtHelper.REFRESH_ID)
                .asLong();

        loginRepository.deleteById(refreshId);
        redisTemplate.opsForValue().set("token_" + accessToken, accessToken, 5L, TimeUnit.MINUTES);
    }

    public void removeAllTokens(String accessToken) {
        String email = jwtHelper.getJwtAccessToken(accessToken).getSubject();

        loginRepository.deleteByAppUserEmail(email);
        redisTemplate.opsForValue().set("token_" + accessToken, accessToken, 5L, TimeUnit.MINUTES);
    }

    public String getNewAccessToken(String refreshToken, AppUser appUser) {
        DecodedJWT jwtToken = jwtHelper.getJwtRefreshToken(refreshToken);

        Long refreshId = jwtToken.getClaim(JwtHelper.REFRESH_ID).asLong();
        Optional<RefreshAuth> refreshAuth = loginRepository.findById(refreshId);

        if (refreshAuth.isEmpty()) {
            throw new BadCredentialsException("The refresh token is not valid");
        }

        String role = jwtToken.getClaim(JwtHelper.ROLE).asString();
        String firstName = jwtToken.getClaim(JwtHelper.FIRST_NAME).asString();

        return jwtHelper.generateAccessToken(
                appUser.getUsername(), role, firstName, refreshAuth.get().getId());
    }

    public boolean checkRefreshTokenValidity(String token) {
        return jwtHelper.verifyRefreshToken(token);
    }
}