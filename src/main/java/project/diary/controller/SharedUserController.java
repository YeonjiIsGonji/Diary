package project.diary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.diary.domain.User;
import project.diary.service.DiarySharingService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SharedUserController {

    private final DiarySharingService diarySharingService;

    @GetMapping("/sharedUserList")
    public String showSharedUserList(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {

        model.addAttribute("user", loginUser);

        List<User> sharedUsers = diarySharingService.getSharedUsers(loginUser.getUserId());
        model.addAttribute("sharedUsers", sharedUsers);

        List<User> usersWhoSharedWithMe = diarySharingService.findUsersWhoSharedWithMe(loginUser.getUserId());
        model.addAttribute("usersWhoSharedWithMe", usersWhoSharedWithMe);

        return "sharedUserList";
    }

    @GetMapping("/sharedUser/add")
    public String addFriendForm(Model model) {
        model.addAttribute("user", new User());
        return "addFriend";
    }

    @PostMapping("/sharedUser/add")
    public String addFriend(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                            @ModelAttribute User user, Model model) {
        try {
            diarySharingService.shareDiary(loginUser.getUserId(), user.getLoginId());
            return "redirect:/sharedUserList";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "addFriend";
        }
    }

    @PostMapping("/sharedUser/delete")
    public String deleteFriend(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                               @RequestParam Long friendId) {
        diarySharingService.deleteSharedUser(loginUser.getUserId(), friendId);
        return "redirect:/sharedUserList";
    }
}
