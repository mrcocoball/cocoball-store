package dev.be.modulecore.repositories.security;

import dev.be.modulecore.domain.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKey(String key);

    void deleteByKey(String key);

}
