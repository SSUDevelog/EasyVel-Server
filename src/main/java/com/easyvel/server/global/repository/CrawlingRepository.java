package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlingRepository extends JpaRepository<Target, Long> {
    List<Target> findAll();
}
