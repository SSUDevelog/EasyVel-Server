package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Target getByVelogUserName(String velogUserName);
}
