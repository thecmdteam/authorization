package com.cmd.authorization.services;

import com.cmd.authorization.model.User;
import com.cmd.authorization.repositories.UserRepository;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TokenService {

    private final VerificationTokenRepo tokenRepo;
    private final UserRepository userRepository;

    public TokenService(VerificationTokenRepo tokenRepo, UserRepository userRepository) {
        this.tokenRepo = tokenRepo;
        this.userRepository = userRepository;
    }

    public String verifyToken(String tokenId) {
        var optionalToken = tokenRepo.findById(tokenId);

        if(optionalToken.isPresent()) {
            var token = optionalToken.get();
            tokenRepo.delete(token);
            var presentDate = new Date(System.currentTimeMillis());
            if(presentDate.after(token.getExpiryDate())) {
                return "expired token";
            }
            var optionalUser = userRepository.findByEmail(token.getUserEmail());
            if (optionalUser.isEmpty()) {
                return "user doesn't exist";
            }
            var user = optionalUser.get();
            enableUser(user);
            return "succes";
        }
        return "invalid token";
    }

    private void enableUser(User user) {
        user.setLastUpdated(new Date(System.currentTimeMillis()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAuthorities(List.of("USER"));
    }

}
