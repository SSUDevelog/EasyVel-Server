package com.easyvel.server.notification.bridge;

import com.easyvel.server.notification.fcm.FcmToken;
import com.easyvel.server.notification.noticegroup.NoticeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoticeGroupFcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private NoticeGroup noticeGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private FcmToken fcmToken;

    public NoticeGroupFcmToken(NoticeGroup noticeGroup, FcmToken fcmToken) {
        this.noticeGroup = noticeGroup;
        this.fcmToken = fcmToken;
    }
}
