package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserRegistrationData;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
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
        if(found !=null){
            throw new UserEmailDuplicationExeption(email);
        }
        //TODO : 제대로 해주자!
        User user = mapper.map(userRegistrationData, User.class);
        return userRepository.save(user);
    }
}
