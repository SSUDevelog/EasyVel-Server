package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Subscribe;
import com.easyvel.server.global.entity.Target;
import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {
    // Todo: 리턴타입을 Optional로 수정했으므로, 관련 로직들 추후 수정하기
    Optional<Subscribe> findByUser(User user);
    Optional<Subscribe> getByUserAndTarget(User user, Target target);
}