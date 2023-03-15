package com.fjss23.jobsearch.auth.login;

import com.fjss23.jobsearch.security.jwt.JwtHelper;
import com.fjss23.jobsearch.user.AppUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    private final JwtHelper jwtHelper;
    private final LoginRepository loginRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public LoginService(JwtHelper jwtHelper, LoginRepository loginRepository,
                        RedisTemplate<String, String> redisTemplate) {
        this.jwtHelper = jwtHelper;
        this.loginRepository = loginRepository;
        this.redisTemplate = redisTemplate;
    }

    public RefreshTokenInfo generateRefreshToken(AppUser appUser, String location, String device) {
        RefreshAuth refreshAuth = new RefreshAuth(location, device, appUser.getEmail());
        Long id = loginRepository.create(refreshAuth);
        String token = jwtHelper.generateRefreshToken(appUser.getUsername(), id);
        return new RefreshTokenInfo(token, id);
    }

    public String generateAccessToken(AppUser appUser, Long refreshId) {
        return jwtHelper.generateAccessToken(
            appUser.getUsername(),
            appUser.getUserRole().name(),
            appUser.getFirstName(),
            refreshId
        );
    }

    public void deleteTokens(String accessToken) {
        Long refreshId = jwtHelper.getIdFromAccessToken(accessToken);
        loginRepository.deleteById(refreshId);
        redisTemplate.opsForValue().set("token_" + accessToken, accessToken, 5L, TimeUnit.MINUTES);
    }

}
