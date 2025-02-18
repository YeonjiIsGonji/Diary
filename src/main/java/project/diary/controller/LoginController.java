package project.diary.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.diary.domain.User;
import project.diary.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") User user) {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletResponse response) {
        User loginUser = loginService.login(user.getLoginId(), user.getPassword());

        if (loginUser == null) {
            return "loginForm";
        }

        //쿠키
        Cookie idCookie = new Cookie("userId", String.valueOf(loginUser.getUserId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
