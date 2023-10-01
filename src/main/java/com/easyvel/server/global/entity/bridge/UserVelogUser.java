package com.easyvel.server.global.entity.bridge;

import com.easyvel.server.global.entity.User;
import com.easyvel.server.global.entity.VelogUser;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class UserVelogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private VelogUser velogUser;

}
