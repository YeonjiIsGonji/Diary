package project.diary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.diary.domain.User;
import project.diary.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("user") User user) {
        return "addUserForm";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/";
    }
}
