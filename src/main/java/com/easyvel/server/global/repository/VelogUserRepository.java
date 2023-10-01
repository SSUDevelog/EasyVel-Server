package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.VelogUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VelogUserRepository extends JpaRepository<VelogUser, Long> {
    Optional<VelogUser> getByName(String name);
}
