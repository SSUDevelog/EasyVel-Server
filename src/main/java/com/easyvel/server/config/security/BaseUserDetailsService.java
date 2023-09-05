package com.easyvel.server.config.security;

import com.easyvel.server.sign.User;
import com.easyvel.server.sign.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BaseUserDetailsService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
        Optional<User> optionalMember = userRepository.getByUid(username);
        if (optionalMember.isEmpty())
            throw new UsernameNotFoundException(username);

        UserDetails userDetails = userRepository.getByUid(username).get();
        return userDetails;
    }
}
