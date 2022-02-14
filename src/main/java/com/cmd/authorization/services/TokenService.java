package com.cmd.authorization.services;

import com.cmd.authorization.model.User;
import com.cmd.authorization.model.enums.VerifyTokenStates;
import com.cmd.authorization.repositories.UserRepository;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public record TokenService(VerificationTokenRepo tokenRepo,
                           UserRepository userRepository) {

    public VerifyTokenStates verifyToken(String tokenId) {
        var optionalToken = tokenRepo.findById(tokenId);

        if (optionalToken.isPresent()) {
            var token = optionalToken.get();
            tokenRepo.delete(token);
            var presentDate = new Date(System.currentTimeMillis());
            if (presentDate.after(token.getExpiryDate())) {
                return VerifyTokenStates.EXPIRED_TOKEN;
            }
            var optionalUser = userRepository.findByUsername(token.getUsername());
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
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        userRepository.save(user);
    }

}
