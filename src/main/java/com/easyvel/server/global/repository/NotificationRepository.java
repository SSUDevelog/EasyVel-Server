package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.NoticeTarget;
import com.easyvel.server.global.entity.Notification;
import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findByUser(User user);
    Notification getByUserAndNoticeTarget(User user, NoticeTarget noticeTarget);
}
