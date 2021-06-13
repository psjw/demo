package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.errors.LoginFailException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login() {
        return jwtUtil.encode(1L);
    }

    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);

        //isEmpty ""만 가능 isBlank " " 띄어쓰기도 가능
//        if (accessToken == null || accessToken.isBlank()) {
//            throw new InvalidTokenException(accessToken);
//        }
//        try {
//            Claims claims = jwtUtil.decode(accessToken);
//            return claims.get("userId", Long.class);
//        } catch (SignatureException e) {
//            throw new InvalidTokenException(accessToken);
//        }
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new LoginFailException(email));
        if ( !user.authenticate(password)) {
            throw new LoginFailException(email);
        }

        return jwtUtil.encode(1L);
    }
}
