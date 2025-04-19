package project.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.diary.domain.Diary;
import project.diary.domain.EmotionType;
import project.diary.domain.LikeResponse;
import project.diary.domain.User;
import project.diary.service.DiaryService;
import project.diary.service.LikeService;

import java.util.List;

@Tag(name = "Diary API")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;
    private final LikeService likeService;

    @GetMapping("/owner")
    @Operation(summary = "로그인한 사용자의 diary 목록 조회")
    public String diaries(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {

//        List<Diary> diaries = diaryService.findAllDiaries();
        List<Diary> diaries = diaryService.findDiariesByUserId(loginUser.getUserId());
        model.addAttribute("diaries", diaries);
        return "diaries";
    }

    @GetMapping("/shared")
    @Operation(summary = "로그인한 사용자가 공유받은 diary 목록 조회")
    public String sharedDiaries(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {

        List<Diary> diaries = diaryService.findSharedDiaries(loginUser.getUserId());
        model.addAttribute("diaries", diaries);
        return "sharedDiaries";
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "특정 diary 상세 조회")
    public String diary(@PathVariable Long diaryId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() ->
        new IllegalArgumentException("Invalid diary Id"));

        boolean isOwner = diary.getAuthorId().equals(loginUser.getUserId()); //로그인 유저가 다이어리 작성자인지 여부 확인
        model.addAttribute("diary", diary);
        model.addAttribute("isOwner", isOwner);

        int likeCount = likeService.countLikes(diaryId);
        model.addAttribute("likeCount", likeCount);

        if (!isOwner) {
            boolean isLiked = likeService.isLiked(diaryId, loginUser.getUserId());
            model.addAttribute("isLiked", isLiked);
        }

        model.addAttribute("user", loginUser);

        return "diary";
    }

    @PostMapping("/{diaryId}/like")
    @ResponseBody
    public LikeResponse toggleLike(@PathVariable Long diaryId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        boolean liked = likeService.toggleLike(diaryId, loginUser.getUserId());
        int count = likeService.countLikes(diaryId);

        return new LikeResponse(liked, count);
    }

    @GetMapping("/add")
    @Operation(summary = "diary 작성 양식 불러오기")
    public String addForm(Model model) {
        model.addAttribute("diary", new Diary());
        model.addAttribute("emotions", EmotionType.values());
        //Thmeleaf에서 th:field 속성을 사용할 때 바인딩 객체가 반드시 필요함. 값을 넘겨주지 않으면 예외 발생
        return "addDiary";
    }

    @PostMapping("/add")
    @Operation(summary = "새로운 diary 저장")
    public String addForm(@ModelAttribute Diary diary, RedirectAttributes redirectAttributes,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {

        diary.setAuthorId(loginUser.getUserId());
        Diary savedDiary = diaryService.saveDiary(diary);
        redirectAttributes.addAttribute("diaryId", savedDiary.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/diaries/{diaryId}";
    }

    @GetMapping("/{diaryId}/edit")
    @Operation(summary = "특정 diary 수정")
    public String edit(@PathVariable Long diaryId, Model model,
                       @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                       RedirectAttributes redirectAttributes) {

        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID"));

        if (!diary.getAuthorId().equals(loginUser.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "작성자만 수정할 수 있습니다.");
            return "redirect:diaries/" + diaryId;
        }
        model.addAttribute("diary", diary);
        model.addAttribute("emotions", EmotionType.values());
        return "editDiary";
    }

    @PostMapping("/{diaryId}/edit")
    @Operation(summary = "특정 diary 수정 후 내역 조회")
    public String edit(@PathVariable Long diaryId, @ModelAttribute Diary diary) {
        diaryService.updateDiary(diaryId, diary);
        return "redirect:/diaries/{diaryId}";
    }

    @PostMapping("/{diaryId}/delete")
    @Operation(summary = "특정 diary 삭제")
    public String delete(@PathVariable Long diaryId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                         RedirectAttributes redirectAttributes) {

        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() -> new IllegalArgumentException("Invalid diary Id"));

        if (!diary.getAuthorId().equals(loginUser.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "작성자만 삭제할 수 있습니다.");
            return "redirect:diaries/" + diaryId;
        }

        diaryService.deleteDiary(diaryId);
        return "redirect:/diaries/owner";
    }
}
