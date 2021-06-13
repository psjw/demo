package com.codesoom.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    private boolean deleted = false;

    public void changeWith(User source) {
        name = source.getName();
        password = source.getPassword();
    }

    //탈퇴했을때 시간같은거 넣어도됨
    public void destroy() {
        deleted = true;
    }

    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
