package com.codesoom.demo.domain;

public interface UserRepository {
    User save(User user);

    User existsByEmail(String email);
}
