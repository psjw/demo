package com.codesoom.demo.infra;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Primary
public interface JpaUserRepository
        extends UserRepository, CrudRepository<User, Long> {
    User save(User user);

    User existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

}
