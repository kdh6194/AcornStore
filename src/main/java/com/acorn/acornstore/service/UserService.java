package com.acorn.acornstore.service;

import com.acorn.acornstore.domain.ProductImage;
import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.domain.UserProfileImage;
import com.acorn.acornstore.domain.UserStatus;
import com.acorn.acornstore.domain.repository.UserRepository;
import com.acorn.acornstore.web.dto.SignUpDTO;
import com.acorn.acornstore.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final S3Uploader s3Uploader;
    private final String POST_IMAGE_DIR = "newUser";
    // 로그인 실패시 동작하도록 만든 로직 변수
    private Map<String, Integer> loginFailures = new HashMap<>();
    private Map<String, LocalDateTime> lockoutTimes = new HashMap<>();

    // 비밀번호 유효성 검사 로직 변수
    private final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Transactional(readOnly = true)
    public String login(UserDTO userDTO) {
        System.out.println("이건 디티오야"+userDTO);
        System.out.println("이건 디티오 이메일이야"+userDTO.getEmail());
        if(userDTO == null || userDTO.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (UserStatus.DELETED.name().equals(userDTO.getUserStatus())) {
            String userStatusMessage = UserStatus.valueOf(userDTO.getUserStatus()).getMessage();
            System.out.println("현재 회원 상태 : " + userStatusMessage);
            throw new RuntimeException("This account has been deleted.");
        }

        if (isLockedOut(email)) {
            throw new RuntimeException("Account is locked. Please try again later.");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            recordLoginFailure(email);
            return new RuntimeException("User not found");
        });
        if(passwordEncoder.matches(password, user.getPassword())){
            resetLoginFailures(email);

            // User DTO에 ID 값 설정
            userDTO.setId(user.getId());

            String token = jwtService.create(userDTO);
            return token;
        }else{
            recordLoginFailure(email);
            throw new RuntimeException("Invalid password");
        }

    }

    @Transactional
    public User signUp(SignUpDTO signUpDTO) {
        if(signUpDTO == null || signUpDTO.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
            String email = signUpDTO.getEmail();
            String password = signUpDTO.getPassword();
            String phone = signUpDTO.getPhone();
            String address = signUpDTO.getAddress();
            String gender = signUpDTO.getGender();
            String about_me = signUpDTO.getAbout_me();
            List<MultipartFile> profileImg = signUpDTO.getProfileImg();

        if(userRepository.existsByEmail(email)){
            log.error("An error occurred: {}", email);
            throw new RuntimeException("Email already exists");
            // 이미 등록된 이메일이라면 가입 실패
        };

        if(!validatePassword(password)){
            throw new RuntimeException("Invalid Password");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        List<String> imageUrls = s3Uploader.upload(signUpDTO.getProfileImg(), POST_IMAGE_DIR);
        for(String img : imageUrls){
            System.out.println("순서대로이미지주소"+img);
        }


        User newUser = User.builder()
                .email(email)
                .password(encryptedPassword)
                .phone(phone)
                .address(address)
                .gender(gender)
                .about_me(about_me)
                .build();

        List<UserProfileImage> profileImages = imageUrls.stream()
                .map(imageUrl -> new UserProfileImage(imageUrl, newUser))
                .collect(Collectors.toList());

        newUser.setProfileImages(profileImages);  // Add images to the user

        for (UserProfileImage userProfileImage : profileImages) {
            userProfileImage.setUser(newUser);
        }

        return userRepository.save(newUser);
    }



    private void recordLoginFailure(String email) {
        int failures = loginFailures.getOrDefault(email, 0) + 1;
        loginFailures.put(email, failures);

        if (failures >= 5) {
            // 계정 잠금 설정 및 잠금 해제 시간 설정 (10분 후)
            lockoutTimes.put(email, LocalDateTime.now().plusMinutes(10));
        }
    }

    private boolean isLockedOut(String email) {
        LocalDateTime lockoutTime = lockoutTimes.get(email);
        return lockoutTime != null && LocalDateTime.now().isBefore(lockoutTime);
    }

    private void resetLoginFailures(String email) {
        loginFailures.remove(email);
        lockoutTimes.remove(email);
    }

    public boolean validatePassword(String password) {
        // 비밀번호 길이 확인
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // 영문 대문자, 소문자, 숫자, 특수기호 포함 여부 확인
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(ch)) { // 공백 문자는 특수기호로 취급하지 않음
                hasSpecialChar = true;
            }

            // 모든 조건을 만족하면 더 이상 검사할 필요 없음
            if (hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
                break;
            }
        }

        return  pattern.matcher(password).matches() &&
                (hasUpperCase && hasLowerCase && hasDigit &&
                        !password.contains(" "));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setUserStatus(UserStatus.DELETED);
    }

}
