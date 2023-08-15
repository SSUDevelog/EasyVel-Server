package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUid(String uid);
}
