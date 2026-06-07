package com.oauth.repository;

import com.oauth.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, Integer> {
    Optional<OAuthUser> findByEmail(String email);
    Optional<OAuthUser> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByEmail(String email);
}
