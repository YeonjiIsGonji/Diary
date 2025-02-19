package project.diary;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String home(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "home";
        }

        model.addAttribute("user", loginUser);
        return "loginHome";
    }
}
