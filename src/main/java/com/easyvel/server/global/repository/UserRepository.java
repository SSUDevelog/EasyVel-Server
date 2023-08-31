package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Todo: 리턴타입을 Optional로 수정했으므로, 관련 로직들 추후 수정하기
    Optional<User> getByUid(String uid);
}
