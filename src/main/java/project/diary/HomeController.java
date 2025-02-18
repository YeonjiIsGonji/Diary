package project.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import project.diary.domain.User;
import project.diary.repository.DiaryRepsitory;
import project.diary.repository.UserRepository;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DiaryRepsitory diaryRepsitory;
    private final UserRepository userRepository;


    @GetMapping("/")
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model) {
        if (userId == null) {
            return "home";
        }

        User loginUser = userRepository.findByID(userId).get();
        if (loginUser == null) {
            return "home";
        }

        model.addAttribute("user", loginUser);
        return "loginHome";
    }
}
