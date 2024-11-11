package com.ex.TestSecurityBasic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {

        // 아이디 확인
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        // 역할 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 로그인한 사용자의 role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("id", id);
        model.addAttribute("role",role);
        return "main";
    }
}
