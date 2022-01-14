package com.cmd.authorization.services;

import com.cmd.authorization.model.User;
import com.cmd.authorization.model.VerifyTokenStates;
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

    public VerifyTokenStates verifyToken(String tokenId) {
        var optionalToken = tokenRepo.findById(tokenId);

        if(optionalToken.isPresent()) {
            var token = optionalToken.get();
            tokenRepo.delete(token);
            var presentDate = new Date(System.currentTimeMillis());
            if(presentDate.after(token.getExpiryDate())) {
                return VerifyTokenStates.EXPIRED_TOKEN;
            }
            var optionalUser = userRepository.findByEmail(token.getUserEmail());
            if (optionalUser.isEmpty()) {
                return VerifyTokenStates.INVALID_TOKEN;
            }
            var user = optionalUser.get();
            enableUser(user);
            return VerifyTokenStates.VALID_TOKEN;
        }
        return VerifyTokenStates.INVALID_TOKEN;
    }

    private void enableUser(User user) {
        user.setLastUpdated(new Date(System.currentTimeMillis()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAuthorities(List.of("USER"));
        userRepository.save(user);
    }

}
