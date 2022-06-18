package com.epam.esm.service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * UserServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 25.04.22
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        log.info("Searching for user - userId: {}", id);
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info("Searching for all users - pageable: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public User findByUsername(String username) {
        log.info("Searching for user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
    }
}
