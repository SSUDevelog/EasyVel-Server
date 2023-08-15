package com.easyvel.server.global.repository;

import com.easyvel.server.global.entity.Subscribe;
import com.easyvel.server.global.entity.Target;
import com.easyvel.server.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {

    Subscribe findByUser(User user);
    Subscribe getByUserAndTarget(User user, Target target);
}