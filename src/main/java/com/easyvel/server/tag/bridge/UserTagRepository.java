package com.easyvel.server.tag.bridge;

import com.easyvel.server.global.entity.User;
import com.easyvel.server.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    Optional<UserTag> findByUserAndTag(User user, Tag tag);
}
