package project.diary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.diary.domain.FriendRequest;
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

    @GetMapping("/friendRequestList")
    public String showFriendRequestList(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        model.addAttribute("user", loginUser);

        List<FriendRequest> requestsToMe = diarySharingService.getPendingRequestsToMe(loginUser.getUserId()); // 내게 보낸 친구 요청
        model.addAttribute("requestsToMe", requestsToMe);

        List<FriendRequest> requestsFromMe = diarySharingService.getPendingRequestsFromMe(loginUser.getUserId()); // 내가 보낸 친구 요청
        model.addAttribute("requestsFromMe", requestsFromMe);

        return "friendRequestList";
    }

    @PostMapping("/friendRequest/accept")
    public String acceptFriendRequest(
            @RequestParam Long requestId,
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            RedirectAttributes redirectAttributes) {

        diarySharingService.acceptFriendRequest(requestId, senderId, receiverId);
        redirectAttributes.addFlashAttribute("message", "친구 요청을 수락하였습니다.");
        return "redirect:/friendRequestList";
    }

    @PostMapping("/friendRequest/reject")
    public String rejectFriendRequest(@RequestParam Long requestId, RedirectAttributes redirectAttributes) {
        diarySharingService.rejectFriendRequest(requestId);
        redirectAttributes.addFlashAttribute("message", "친구 요청을 거절하였습니다.");
        return "redirect:/friendRequestList";
    }

    @GetMapping("/friendRequest/add")
    public String addFriendForm(Model model) {
        model.addAttribute("user", new User());
        return "addFriend";
    }

    @PostMapping("/friendRequest/add")
    public String addFriend(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                            @ModelAttribute User user, Model model) {

        String message = diarySharingService.sendFriendRequest(loginUser.getUserId(), user.getLoginId());

        if (!message.equals("초대 요청이 전송되었습니다.")) {
            model.addAttribute("errorMessage", message); // 오류 메시지 추가
            model.addAttribute("user", new User());
            return "addFriend"; // 다시 친구 추가 페이지로 이동
        }

        return "redirect:/friendRequestList"; // 친구 요청 목록으로 이동
//        try {
//            diarySharingService.sendFriendRequest(loginUser.getUserId(), user.getLoginId()); //초대 요청 보내기
//            return "redirect:/friendRequestList";
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "addFriend";
//        }
    }

    @PostMapping("/sharedUser/delete")
    public String deleteFriend(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                               @RequestParam Long friendId) {
        diarySharingService.deleteSharedUser(loginUser.getUserId(), friendId);
        return "redirect:/sharedUserList";
    }
}
