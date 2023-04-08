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

    public record GenerateRefreshToken(String token, Long id) { }

    public LoginService(
            JwtHelper jwtHelper, LoginRepository loginRepository, RedisTemplate<String, String> redisTemplate) {
        this.jwtHelper = jwtHelper;
        this.loginRepository = loginRepository;
        this.redisTemplate = redisTemplate;
    }

    public GenerateRefreshToken generateRefreshToken(AppUser appUser, String location, String device) {
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(location, device, appUser.getEmail());
        Long id = loginRepository.create(refreshTokenInfo);

        String token = jwtHelper.generateRefreshToken(
                appUser.getUsername(), appUser.getUserRole().name(), appUser.getFirstName(), id);
        return new GenerateRefreshToken(token, id);
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

    public String getNewAccessToken(String refreshToken) {
        DecodedJWT jwtToken = jwtHelper.getJwtRefreshToken(refreshToken);

        Long refreshId = jwtToken.getClaim(JwtHelper.REFRESH_ID).asLong();
        Optional<RefreshTokenInfo> refreshAuth = loginRepository.findById(refreshId);

        if (refreshAuth.isEmpty()) {
            throw new BadCredentialsException("The refresh token is not valid");
        }

        String role = jwtToken.getClaim(JwtHelper.ROLE).asString();
        String username = jwtToken.getSubject();
        String firstName = jwtToken.getClaim(JwtHelper.FIRST_NAME).asString();

        return jwtHelper.generateAccessToken(username, role, firstName, refreshAuth.get().getId());
    }

    public boolean checkRefreshTokenValidity(String token) {
        return jwtHelper.verifyRefreshToken(token);
    }
}
