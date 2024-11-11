package com.ex.TestSecurityBasic.controller;

import com.ex.TestSecurityBasic.dto.JoinDTO;
import com.ex.TestSecurityBasic.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class JoinController {

    // 후에 꼭 필드 주입 -> 생성자 주입으로 바꿔야 함
    @Autowired
    private JoinService joinService;


    @GetMapping("/join")
    public String joinP() {
        // join 페이지 반환
        return "join";
    }

    // 데이터 전송
    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());

        joinService.joinProcess(joinDTO);

        return "redirect:/login";
    }
}
