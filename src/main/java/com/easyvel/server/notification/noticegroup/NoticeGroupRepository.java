package com.easyvel.server.notification.noticegroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeGroupRepository extends JpaRepository<NoticeGroup, Long> {
    Optional<NoticeGroup> findNoticeGroupByValue(String value);
}
