package com.acorn.acornstore.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;



@Configuration
@EnableEncryptableProperties
public class SecretKeyGenerator {

    @Value("${spring.jasypt.encryptor.password}")
    private String jasyptEncryptorPassword;

    @Bean
    public String jasyptEncryptorPassword(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        String jasyptEncryptorPassword = Base64.getEncoder().encodeToString(key);

        return jasyptEncryptorPassword;
    }

    @Bean
    public String encryptedSecretKey(){

        return null;
    }
}