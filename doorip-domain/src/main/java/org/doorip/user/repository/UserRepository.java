package org.doorip.user.repository;

import org.doorip.user.domain.Platform;
import org.doorip.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByPlatformAndPlatformId(Platform platform, String platformId);

    boolean existsUserByPlatformAndPlatformId(Platform platform, String platformId);
}
