package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.service.UserService;
import com.acorn.acornstore.web.dto.SignUpDTO;
import com.acorn.acornstore.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // 지금 문제는 프론트에서 가져오는 데이터 타입이
    // application/json 형식으로 가져와야하는데 그렇고 있지 못한 상황
    // postman으로 진행하면 문제는 없다.(비밀번호 유효성 검사도 잘 작동함)
    @PostMapping(value="/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> signUp(@Valid @RequestBody SignUpDTO signUpDTO){
        System.out.println("유저" + signUpDTO);
        User isSignUp = userService.signUp(signUpDTO);

        if (isSignUp != null) {
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이미 등록된 이메일입니다."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> signIn(@RequestBody UserDTO userDTO, HttpServletResponse httpServletResponse) throws Exception{
            String token = userService.login(userDTO);
            Map<String, String> response = new HashMap<>();

        if (token != null) {
            httpServletResponse.setHeader("Authorization", "Bearer " + token);
            response.put("message", "로그인이 성공적으로 완료되었습니다.");
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
