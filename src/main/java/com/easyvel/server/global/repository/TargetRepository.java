package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> getByVelogUserName(String velogUserName);
}
