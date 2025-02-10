package com.example.demo.controller;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.component.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Controller
@RequestMapping("/page")
public class PageController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public String index(
        @RequestParam(value = "message", required = false, defaultValue = "您好Java Spring!") String message,
        @RequestParam(value = "description", required = false, defaultValue = "我是描述!") String description,
        Model model
    ) {
        model.addAttribute("message", message);
        model.addAttribute("description", description);
        return "index";
    }

    @GetMapping("/card")
    public String card(
        Model model
    ) {
        return "card";
    }

    @GetMapping("/example")
    public String example(
        Model model
    ) {
        return "example";
    }

    @GetMapping("/jquery")
    public String jquery(
        Model model
    ) {
        return "jquery";
    }

    @GetMapping("/checkusername")
    public String checkusername(
        Model model
    ) {
        return "checkusername";
    }

    @GetMapping("/upload")
    public String upload(
        Model model
    ) {
        return "upload";
    }

    @GetMapping("/login")
    public String login(
        Model model
    ) {
        return "login";
    }

    @GetMapping("/success")
    public String login_success(
        @AuthenticationPrincipal OAuth2User principal,
        Model model
    ) {
        String name = "";
        String email = "";
        Collection<? extends GrantedAuthority> roles = List.of(); // 初始化角色列表
        if (principal != null) {
            name = principal.getAttribute("name");
            email = principal.getAttribute("email");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            name = authentication.getName();
            roles = authentication.getAuthorities();
        }
        
        model.addAttribute("name", name);
        model.addAttribute("email", email);

        List<String> roleNames = roles.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        String token = jwtTokenUtil.generateToken(name, roleNames);

        model.addAttribute("token", token);

        return "success";
    }
    

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
