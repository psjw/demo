// 암호화: 평문 -> 암호(123) -> (456)
// 복호화 : 암호 -> 평문 (456) -> (123)
// 복호화가 불가능한 암호화: 123 -> 6
// Hash -> data -> n-bit 일정한 길이로 바꿔줌
// Hash Table (key(String) -> intger(32-bit)) -> Hash 충돌이 적을 수록 성능이 좋음
// 123 -> 6
// 123 -> 6
// 456 -> 15
// 321 -> 6
// 충돌저항성
// 역상저항성 :복호화 못함
// 제2역상저항성
// 123 -> 6
// 6 -> 123 못만듬 :역상저항성
// 321 -> 6 안됨 : 제2역상저항성
package com.codesoom.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Builder.Default
    private String email="";

    @Builder.Default
    private String name ="";

    @Builder.Default
    private String password = "";


    @Builder.Default
    private boolean deleted = false;


    public void changeWith(User source) {
        name = source.getName();
    }

    //탈퇴했을때 시간같은거 넣어도됨
    public void destroy() {
        deleted = true;
    }

    public boolean authenticate(String password, PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password,this.password);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
