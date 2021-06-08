package com.codesoom.demo.controllers;
//ToDo
// 1. 가입 -> Post /users => 가입정보(DTO) -> email이 unique key!
// 2. 목록, 상세 보기 -> ADMIN!
// 3. 사용자 정보 갱신 -> PUT/PATCH /users/{id} => 정보갱신(DTO) -> 이름만
// ==> 권한 확인.. Authorization --> 다음 시간에...
// 4. 탈퇴 -> DELETE /users/{id} => soft delete

import com.codesoom.demo.application.UserService;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserModificationData;
import com.codesoom.demo.dto.UserRegistrationData;
import com.codesoom.demo.dto.UserResultData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData userRegistrationDataData) {
        User user = userService.registerUser(userRegistrationDataData);
        return getUserResultData(user);
    }

    @PatchMapping("{id}")
    UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationData modificationData) {
        //TODO
        User user = userService.updateUser(id, modificationData);
        return getUserResultData(user);
    }

    @DeleteMapping("{id}")
    void destory(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResultData getUserResultData(User user) {
        if (user == null) {
            return null;
        }
        return UserResultData.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }


}
