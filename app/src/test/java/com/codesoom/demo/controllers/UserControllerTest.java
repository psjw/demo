package com.codesoom.demo.controllers;

import com.codesoom.demo.application.UserService;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserRegistrationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        given(userService.registerUser(any(UserRegistrationData.class))).will(invocation -> {
            UserRegistrationData userRegistrationData = invocation.getArgument(0);
            User user = User.builder()
                    .id(13L)
                    .name(userRegistrationData.getName())
                    .email(userRegistrationData.getEmail())
                    .build();
            return user;
        });
    }


    @Test
    void registerUserWithValidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"tester\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"id\":13")
                ))
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"tester\"")
                ));

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @Test
    void registerUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

}