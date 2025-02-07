package project.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.diary.domain.Diary;
import project.diary.domain.EmotionType;
import project.diary.service.DiaryService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/record/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public String diaries(Model model) {
        List<Diary> diaries = diaryService.findAllDiaries();
        model.addAttribute("diaries", diaries);
        return "diaries";
    }

    @GetMapping("/{diaryId}")
    public String diary(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() ->
        new IllegalArgumentException("Invalid diary Id"));
        model.addAttribute("diary", diary);
        return "diary";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("diary", new Diary());
        model.addAttribute("emotions", EmotionType.values());
        //Thmeleaf에서 th:field 속성을 사용할 때 바인딩 객체가 반드시 필요함. 값을 넘겨주지 않으면 예외 발생
        return "addDiary";
    }

    @PostMapping("/add")
    public String addForm(@ModelAttribute Diary diary, RedirectAttributes redirectAttributes) {
        Diary savedDiary = diaryService.saveDiary(diary);
        redirectAttributes.addAttribute("diaryId", savedDiary.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/record/diaries/{diaryId}";
    }

    @GetMapping("/{diaryId}/edit")
    public String edit(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID"));
        model.addAttribute("diary", diary);
        model.addAttribute("emotions", EmotionType.values());
        return "editDiary";
    }

    @PostMapping("/{diaryId}/edit")
    public String edit(@PathVariable Long diaryId, @ModelAttribute Diary diary) {
        diaryService.updateDiary(diaryId, diary);
        return "redirect:/record/diaries/{diaryId}";
    }

    @PostMapping("/{diaryId}/delete")
    public String delete(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return "redirect:/record/diaries";
    }
}
