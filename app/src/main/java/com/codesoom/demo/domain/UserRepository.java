package com.codesoom.demo.domain;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdDeletedIsFalse(Long id);
}
