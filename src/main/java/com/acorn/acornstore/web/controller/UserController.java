package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.service.UserService;
import com.acorn.acornstore.web.dto.SignUpDTO;
import com.acorn.acornstore.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
@Validated
public class UserController {
    private final UserService userService;
    @GetMapping("/signup")
    public String showSignUp(){ // ResponseEntity로 데이터 타입 잡고

        return "signup";
    }

    @GetMapping("/login")
    public String showSignIn(){ // ResponseEntity로 데이터 타입 잡고

        return "signin";
    }

    @PostMapping(value="/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,String>> signUp(@Valid @ModelAttribute SignUpDTO signUpDTO, @RequestParam("profileImg")MultipartFile img) {
        signUpDTO.setProfileImg(img);
        System.out.println("받아온 유저 정보" + signUpDTO.getEmail());
        User isSignUp = userService.signUp(signUpDTO);
        System.out.println("가입한 유저 정보" + isSignUp.getEmail());
        if (isSignUp != null) {
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이미 등록된 이메일입니다."));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String,String>> signIn(@ModelAttribute UserDTO userDTO, HttpServletResponse httpServletResponse) throws Exception{
            String token = userService.login(userDTO);
            Map<String, String> response = new HashMap<>();
            System.out.println("토큰이 헤더에 무엇이 있나?"+httpServletResponse.getHeader("Authorization"));
        System.out.println("여기엔 무엇이 들어있어???"+token);
        if (token != null) {
            httpServletResponse.setHeader("Authorization", "Bearer " + token);
            response.put("message", "로그인이 성공적으로 완료되었습니다.");
            System.out.println("토큰이 헤더에 입력되는가?"+httpServletResponse.getHeader("Authorization"));
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "로그인 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // 로그아웃 처리는 스프링시큐리티를 통해 처리하여 config클래스에서 정리 따로 엔드 포인트 줄 필요가 없다 생각

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestBody LogoutRequest logoutRequest) {
//        boolean isLoggedOut = userService.logout(logoutRequest.getEmail());
//
//        if (isLoggedOut) {
//            return ResponseEntity.ok("로그아웃되었습니다.");
//        } else {
//            return ResponseEntity.badRequest().body("로그아웃 실패");
//        }
//    }

}
