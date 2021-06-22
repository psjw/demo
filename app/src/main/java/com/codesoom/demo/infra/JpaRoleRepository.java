package com.codesoom.demo.infra;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Primary
public interface JpaRoleRepository extends RoleRepository, CrudRepository<Role, Long> {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
