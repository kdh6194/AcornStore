package com.acorn.acornstore.service;

import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.web.dto.OAuth2UserDTO;
import com.acorn.acornstore.web.dto.UserDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    @Value("${spring.jasypt.properties.encrypted-secretKey}")
    private String secretKey;

    public String create(UserDTO userDTO){
        Date expireAt = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .setSubject(String.valueOf(userDTO.getId()))
                .setIssuer("Acorn")
                .setIssuedAt(new Date())
                .setExpiration(expireAt)
                .compact();
    }

    public String validateAndGetUserId(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        }catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 때의 예외 처리
            log.error("An error occurred: {}", token);
            throw new RuntimeException("Token has expired");
        } catch (MalformedJwtException e) {
            // 토큰이 유효하지 않은 형식일 때의 예외 처리
            log.error("An error occurred: {}",token);
            throw new RuntimeException("Malformed JWT");
        } catch (SignatureException e) {
            // 서명이 올바르지 않을 때의 예외 처리
            log.error("An error occurred: {}", token);
            throw new RuntimeException("JWT signature validation failed");
        }
    }

    public String create(final Authentication authentication){
        OAuth2UserDTO userPrincipal = (OAuth2UserDTO) authentication.getPrincipal();
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(userPrincipal.getName()) // id가 리턴됨.
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }


}
