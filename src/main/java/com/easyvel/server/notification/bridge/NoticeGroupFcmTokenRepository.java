package com.easyvel.server.notification.bridge;

import com.easyvel.server.notification.fcm.FcmToken;
import com.easyvel.server.notification.noticegroup.NoticeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeGroupFcmTokenRepository extends JpaRepository<NoticeGroupFcmToken, Long> {
    Optional<NoticeGroupFcmToken> findNoticeGroupFcmTokenByNoticeGroupAndFcmToken(NoticeGroup noticeGroup, FcmToken fcmToken);
}
