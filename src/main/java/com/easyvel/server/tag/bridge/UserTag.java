package com.easyvel.server.tag.bridge;

import com.easyvel.server.global.entity.User;
import com.easyvel.server.tag.Tag;
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
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public UserTag(User user, Tag tag) {
        this.user = user;
        this.tag = tag;
    }
}
