package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.NoticeTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeTargetRepository extends JpaRepository<NoticeTarget, Long> {

    NoticeTarget getByGroupName(String groupName);
}
