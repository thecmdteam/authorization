package com.cmd.authorization.services;

import com.cmd.authorization.model.User;
import com.cmd.authorization.model.enums.VerifyTokenStates;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public final class TokenService {
    private final VerificationTokenRepo tokenRepo;
    private final JdbcUserDetailsManager userDetailsManager;

    public TokenService(VerificationTokenRepo tokenRepo,
                        UserDetailsService userDetailsManager) {
        this.tokenRepo = tokenRepo;
        this.userDetailsManager = (JdbcUserDetailsManager) userDetailsManager;
    }

    public VerifyTokenStates verifyToken(String tokenId) {
        var optionalToken = tokenRepo.findById(tokenId);

        if (optionalToken.isPresent()) {
            var token = optionalToken.get();
            tokenRepo.delete(token);
            var presentDate = new Date(System.currentTimeMillis());
            if (presentDate.after(token.getExpiryDate())) {
                return VerifyTokenStates.EXPIRED_TOKEN;
            }
            var u = userDetailsManager.loadUserByUsername(token.getUsername());
            if (u == null) {
                return VerifyTokenStates.INVALID_TOKEN;
            }
            var user = mapUser(u);
            userDetailsManager.updateUser(user);
            return VerifyTokenStates.VALID_TOKEN;
        }
        return VerifyTokenStates.INVALID_TOKEN;
    }

    public UserDetails verifyRecoveryToken(String tokenId) {
        var optionalToken = tokenRepo.findById(tokenId);

        if (optionalToken.isPresent()) {
            var token = optionalToken.get();
            tokenRepo.delete(token);
            var presentDate = new Date(System.currentTimeMillis());
            if (presentDate.after(token.getExpiryDate())) {
                return null;
            }
            return userDetailsManager.loadUserByUsername(token.getUsername());
        }
        return null;
    }

    private User mapUser(UserDetails u) {
        var user = new User();
        user.setUsername(u.getUsername());
        user.setPassword(u.getPassword());
        user.setEnabled(true);
        return user;
    }


}
