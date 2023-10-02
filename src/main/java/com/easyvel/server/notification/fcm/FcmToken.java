package com.easyvel.server.notification.fcm;

import com.easyvel.server.global.entity.User;
import com.easyvel.server.notification.bridge.NoticeGroupFcmToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String value;

    @NotNull
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "fcmToken", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeGroupFcmToken> noticeGroupFcmTokenList = new ArrayList<>();

    public FcmToken(String value, User user) {
        this.value = value;
        this.user = user;
    }
}
