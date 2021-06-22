package com.codesoom.demo.application;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserModificationData;
import com.codesoom.demo.dto.UserRegistrationData;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
import com.codesoom.demo.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    private static final Long DELETE_USER_ID = 200L;
    private final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private UserService userService;
    private UserRepository userRepository = mock(UserRepository.class);
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository = mock(RoleRepository.class);

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserService(userRepository, mapper, passwordEncoder, roleRepository);

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);
            return User.builder()
                    .id(13L)
                    .email(source.getEmail())
                    .name(source.getName())
                    .build();
        });

        given(userRepository.findByEmail(EXISTED_EMAIL_ADDRESS))
                .willThrow(new UserEmailDuplicationExeption("tester@example.com"));

        given(userRepository.findByIdAndDeletedIsFalse(1L))
                .willReturn(Optional.of(
                        User.builder()
                                .id(1L)
                                .email(EXISTED_EMAIL_ADDRESS)
                                .name("Tester")
                                .password("test")
                                .build()));
        //TODO : 잘못했음.
//        given(userRepository.findById(1000L))
//                .willThrow(new UserNotFoundException(1000L));
        //ToDO: 변경
        given(userRepository.findByIdAndDeletedIsFalse(100L)).willReturn(Optional.empty());

        given(userRepository.findByIdAndDeletedIsFalse(DELETE_USER_ID))
                .willReturn(Optional.empty());
    }

    @Test
    void registerUser() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email("tester@example.com")
                .name("Tester")
                .password("test")
                .build();
        User user = userService.registerUser(registrationData);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(13L);
        assertThat(user.getEmail()).isEqualTo("tester@example.com");
        assertThat(user.getName()).isEqualTo("Tester");
        verify(userRepository).save(any(User.class));
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void registerWithDuplicatedEmail() {


        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email(EXISTED_EMAIL_ADDRESS)
                .name("Tester")
                .password("test")
                .build();

        assertThatThrownBy(() -> {
            userService.registerUser(registrationData);
        }).isInstanceOf(UserEmailDuplicationExeption.class);

        verify(userRepository).findByEmail(EXISTED_EMAIL_ADDRESS);
    }

    // 에러 #1 - ID없음
    // 에러 #2 - 속성 오류
    @Test
    void updateUserWithExistedId() {
        UserModificationData modficationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();
        Long userId = 1L;
        User user = userService.updateUser(1L, modficationData, userId);
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL_ADDRESS);
        assertThat(user.getName()).isEqualTo("TEST");
        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void updateUserWithNotExistedId() {
        UserModificationData modficationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();
        Long userId = 1000L;
        //잘못함
        assertThatThrownBy(() -> {
            userService.updateUser(userId, modficationData, userId);
        })
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(1000L);
    }
    //TODO : 업데이트 대상과 업데이트 하려는 주체가 다른경우 테스트필요.

    @Test
    void updateUserByOthersAccess() {
        UserModificationData modficationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();
        Long targetUserId = 1L;
        Long currentUserId = 2L;
        //잘못함
        assertThatThrownBy(() -> {
            userService.updateUser(targetUserId, modficationData, currentUserId);
        })
                .isInstanceOf(AccessDeniedException.class);

    }

    @Test
    void updateUserWithDeletedId() {
        UserModificationData modficationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();
        assertThatThrownBy(() -> {
            userService.updateUser(DELETE_USER_ID, modficationData, DELETE_USER_ID);
        }).isInstanceOf(UserNotFoundException.class);
        verify(userRepository).findByIdAndDeletedIsFalse(DELETE_USER_ID);
    }

    @Test
    void deleteUserWithExistedId() {
        User user = userService.deleteUser(1L);
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();
        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void deleteUserWithNotExistedId() {

        assertThatThrownBy(() -> {
            userService.deleteUser(100L);
        }).isInstanceOf(UserNotFoundException.class);
        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }

    @Test
    void deleteUserWithNotDeletedId() {
        assertThatThrownBy(() -> {
            userService.deleteUser(DELETE_USER_ID);
        }).isInstanceOf(UserNotFoundException.class);
        verify(userRepository).findByIdAndDeletedIsFalse(DELETE_USER_ID);
    }
}
