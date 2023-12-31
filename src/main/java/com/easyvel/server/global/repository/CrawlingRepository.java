package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.VelogUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlingRepository extends JpaRepository<VelogUser, Long> {
    List<VelogUser> findAll();
}
