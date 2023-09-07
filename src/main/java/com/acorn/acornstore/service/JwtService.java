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
    @Value("#{encryptedSecretKey}")
    private String secretKey;

    public String create(UserDTO userDTO){
        Date expireAt = Date.from(Instant.now().plus(2, ChronoUnit.HOURS));// 만료 기간을 2시간 후로 설정하는 로직
        String issuer = "Acorn";
        String audience = "AcornUsers";
        String subject = "userToken";

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("userId", String.valueOf(userDTO.getId()));
        claims.put("email", userDTO.getEmail());
        System.out.println("userId"+ userDTO.getId());
        System.out.println("email"+ userDTO.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(new Date())
                .setExpiration(expireAt)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }

//    public String createRefreshToken(UserDTO userDTO) {
//        Date expireAt = Date.from(Instant.now().plus(10,ChronoUnit.DAYS));
//
//        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .setSubject(String.valueOf(userDTO.getId()))
//                .setIssuer("Acorn")
//                .setIssuedAt(new Date())
//                .setExpiration(expireAt)
//                .compact();
//    }

    public String validateAndGetUserId(String token){
        System.out.println(token);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String id = claims.get("userId",String.class);
            String email = claims.get("email",String.class);;


            return id +","+ email;

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

//    Date expireAt = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

//        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS512,secretKey)
//                .setSubject(String.valueOf(userDTO.getId()))
//                .setIssuer("Acorn")
//                .setIssuedAt(new Date())
//                .setExpiration(expireAt)
//                .compact();



//    public Map<String, String> validateAndExtractUserDetails(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String id = claims.get("id", String.class); // 'id'라는 이름의 claim 값을 문자열로 가져옵니다.
//            String email = claims.get("email", String.class); // 'email'이라는 이름의 claim 값을 문자열로 가져옵니다.
//
//            String issuer = claims.getIssuer();
//            String audience = claims.getAudience();
//
//            if (!"Acorn".equals(issuer)) {
//                throw new JwtException("Invalid issuer");
//            }
//
//            if (!"AcornUsers".equals(audience)) {
//                throw new JwtException("Invalid audience");
//            }
//            System.out.println("토큰클레임 " + claims.getSubject());

//            Map<String, String> userDetails = new HashMap<>();
//            userDetails.put("id", id);
//            userDetails.put("email", email);
//
//            return userDetails;
//
//        } catch (ExpiredJwtException e) {
//            // 토큰이 만료되었을 때의 예외 처리
//            log.error("An error occurred: {}", token);
//            throw new RuntimeException("Token has expired");
//        } catch (MalformedJwtException e) {
//            // 토큰이 유효하지 않은 형식일 때의 예외 처리
//            log.error("An error occurred: {}",token);
//            throw new RuntimeException("Malformed JWT");
//        } catch (SignatureException e) {
//            // 서명이 올바르지 않을 때의 예외 처리
//            log.error("An error occurred: {}", token);
//            throw new RuntimeException("JWT signature validation failed");
//        }
//    }