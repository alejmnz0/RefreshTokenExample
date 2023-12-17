package net.openwebinars.springboot.restjwt.security.jwt.refresh;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.openwebinars.springboot.restjwt.security.errorhandling.RefreshTokenException;
import net.openwebinars.springboot.restjwt.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.duration}")
    private int durationMinutes;

    public Optional<RefreshToken> findByToken (String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUser (User user){
        refreshTokenRepository.deleteByUser(user);
    }

    public RefreshToken createRefreshToken (User user){
        return refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expirateDate(Instant.now().plusSeconds(durationMinutes * 60))
                .build()
                );
    }

    public RefreshToken verify (RefreshToken refreshToken){
        if (refreshToken.getExpirateDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("El token ha caducado.");
        }
        return refreshToken;
    }
}
