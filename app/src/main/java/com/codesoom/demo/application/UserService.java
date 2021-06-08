package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserModificationData;
import com.codesoom.demo.dto.UserRegistrationData;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
import com.codesoom.demo.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, Mapper dozerMapper) {
        this.userRepository = userRepository;
        this.mapper = dozerMapper;
    }


    public User registerUser(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getEmail();
        User found = userRepository.existsByEmail(email);
        if (found != null) {
            throw new UserEmailDuplicationExeption(email);
        }
        //TODO : 제대로 해주자!
        User user = mapper.map(userRegistrationData, User.class);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserModificationData modificationData) {
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
