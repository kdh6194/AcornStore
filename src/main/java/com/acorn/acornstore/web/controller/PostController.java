package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class PostController {
    @GetMapping("/home")
    public String start(@AuthenticationPrincipal HttpServletRequest request) {
        System.out.println("User private profile"+request);

        return "layout/start";
    }

    @GetMapping("/how")
    public String how(@AuthenticationPrincipal User user) {
        System.out.println("User private profile"+user);

        return "layout/start";
    }
}
