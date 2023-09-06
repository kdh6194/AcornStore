package com.acorn.acornstore.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


@Configuration
@EnableEncryptableProperties
public class SecretKeyGenerator {
    @Bean
    public Dotenv dotenv() {
        System.out.println("오버라이딩 전 시크릿 키"+Dotenv.load().get("SECRET_KEY"));
        return Dotenv.load();
    }
    @Bean(name = "encryptedSecretKey")
    public String getAndEncryptSecretKey(Dotenv dotenv) {

        String secretKey = dotenv.get("SECRET_KEY");

        if (secretKey == null || secretKey.isEmpty()) {
            throw new RuntimeException("SECRET_KEY is not set in .env file");
        }

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);

        return encryptor.encrypt(secretKey);
    }
}

// env파일에 secretkey가 있다면 덮어쓰기하는 로직
    // Generate a new random secret key and Base64 encode it
//    SecureRandom secureRandom = new SecureRandom();
//    byte[] key = new byte[64];
//        secureRandom.nextBytes(key);
//                String secretKey = Base64.getEncoder().encodeToString(key);
//
//                File envFile = new File(".env");
//
//                if (!envFile.exists()) {
//                // If the .env file does not exist, create it and write the secret key
//                try (FileWriter writer = new FileWriter(envFile)) {
//                writer.write("SECRET_KEY=" + secretKey);
//                writer.flush();
//                }
//                } else {
//                // If the .env file exists, read its content and replace the SECRET_KEY line
//                List<String> lines = Files.readAllLines(envFile.toPath());
//
//        boolean found = false;
//        for (int i = 0; i < lines.size(); i++) {
//        if (lines.get(i).startsWith("SECRET_KEY=")) {
//        lines.set(i, "SECRET_KEY=" + secretKey);
//        found = true;
//        break;
//        }
//        }
//
//        // If no SECRET_KEY line was found, add one at the end of the file
//        if (!found) {
//        lines.add("SECRET_KEY=" + secretKey);
//        }
//
//        // Write back to the .env file
//        Files.write(envFile.toPath(), lines);
//        }
//
//        System.out.println("무작위 생성 시크릿 키 확인용: " + secretKey);
//
//        return secretKey;

//      secretkey가 없으면 env파일에 secretkey를 입력하고 저장하는 로직
//    public String generateAndSaveSecretKey(Dotenv dotenv) throws IOException {
//        // Check if the secret key already exists in the .env file
//        String secretKey = dotenv.get("SECRET_KEY");
//
//        // If the secret key does not exist, generate and save it
//        if (secretKey == null || secretKey.isEmpty()) {
//            // Generate a new random secret key and Base64 encode it
//            SecureRandom secureRandom = new SecureRandom();
//            byte[] key = new byte[64];
//            secureRandom.nextBytes(key);
//            secretKey = Base64.getEncoder().encodeToString(key);
//
//            // Save the generated secret key to the .env file
//            try (FileWriter writer = new FileWriter(".env", true)) {
//                writer.write("SECRET_KEY=" + secretKey);
//                writer.flush();
//            }
//        }
//
//        System.out.println("무작위 생성 시크릿 키 확인용: " + secretKey);
//
//        return secretKey;
//    }
//@Bean(name = "encryptedSecretKey")
//public String encryptSecretKey(@Qualifier("plainSecretKey") String plainSecretkey) {
//    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//    String secretKey = dotenv().get("SECRET_KEY");
//    encryptor.setPassword(secretKey);  // Jasypt에서 사용하는 암호화 키
//
//    return encryptor.encrypt(plainSecretkey);
//}