package com.fjss23.jobsearch.user;

import com.fjss23.jobsearch.registration.token.ConfirmationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserService(AppUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public String signUpUser(AppUser user) {
        boolean exists = userRepository.findByEmail(user.getUsername()).isPresent();

        if (exists) {
            // TODO: Check if the same person is trying to register again
            throw new IllegalArgumentException("email.taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        var confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(20));
        return "";
    }
}
