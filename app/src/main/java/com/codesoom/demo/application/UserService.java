package com.codesoom.demo.application;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserModificationData;
import com.codesoom.demo.dto.UserRegistrationData;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
import com.codesoom.demo.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, Mapper dozerMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.mapper = dozerMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public User registerUser(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getEmail();
        User found = userRepository.findByEmail(email).orElse(null);
        if (found != null) {
            throw new UserEmailDuplicationExeption(email);
        }
        //TODO : 제대로 해주자!
        User user = mapper.map(userRegistrationData, User.class);
        user.changePassword(userRegistrationData.getPassword(),passwordEncoder);
        roleRepository.save(new Role(user.getId(), "USER"));

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserModificationData modificationData, Long userId) {
        if (id != userId){
            throw new AccessDeniedException("Access Denied");
        }
        //TODO : 제대로 할것
        User user = findUser(id);
        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);
        return user;
    }

    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    private User findUser(Long id) {
        return  userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
