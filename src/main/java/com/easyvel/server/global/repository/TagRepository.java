package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Tag;
import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    Tag findByUser(User user);
}
