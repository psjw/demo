package com.codesoom.demo.utils;

import com.codesoom.demo.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    //@Value("1234")
    //Could not autowire. No beans of 'String' type found.  발생시 @Value로 초기 세팅
    //yml에서 앞이 0이면 "" 붙여야된다.
    public JwtUtil(@Value("${jwt.secret}") String secret) {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //HMAC+SH256
        //SignatureAlgorithm.HS256  , 384, 512
        //256사용 : 256 /8bit -> 32
//        secret = "12345678901234567890123456789012";
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
