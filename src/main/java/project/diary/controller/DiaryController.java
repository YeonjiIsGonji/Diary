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
import project.diary.service.DiaryService;

import java.util.List;

@Tag(name = "Diary API")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    @Operation(summary = "diary 목록 조회")
    public String diaries(Model model) {
        List<Diary> diaries = diaryService.findAllDiaries();
        model.addAttribute("diaries", diaries);
        return "diaries";
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "특정 diary 상세 조회")
    public String diary(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() ->
        new IllegalArgumentException("Invalid diary Id"));
        model.addAttribute("diary", diary);
        return "diary";
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
    public String addForm(@ModelAttribute Diary diary, RedirectAttributes redirectAttributes) {
        Diary savedDiary = diaryService.saveDiary(diary);
        redirectAttributes.addAttribute("diaryId", savedDiary.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/diaries/{diaryId}";
    }

    @GetMapping("/{diaryId}/edit")
    @Operation(summary = "특정 diary 수정")
    public String edit(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.findDiaryById(diaryId).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID"));
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
    public String delete(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return "redirect:/diaries";
    }
}
