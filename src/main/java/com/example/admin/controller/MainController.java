package com.example.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    // 여기서 postmapping으로 /login이 없는 이유는? (분명 login.html에서 form action속성에 /login으로 되어있는데도)
    // 이유 : postmapping으로 /login은 controller로 들어오는 요청이 아니고 spring security에서 요청을 가로채고 인증루틴을 실행해주기때문이다.

    // 여기서 getmapping으로 /lougout이 없는 이유?
    // 이유 : 이 또한 controller로 안들어오고 spring security에서 요청을 가로채서 로그아웃 루틴을 실행시켜서 LogoutSuccessUrl로 보낸다.
    // 참고 : securityconfig파일의 주석 참고

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

}
