package project.diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.diary.domain.Diary;
import project.diary.repository.DiaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<Diary> findDiariesByUserId(Long userId) {
        return diaryRepository.findDiariesByUserId(userId);
    }

    public Optional<Diary> findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }

    public Diary saveDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    public void updateDiary(Long diaryId, Diary diary) {
        diaryRepository.update(diaryId, diary);
    }

    public void deleteDiary(Long diaryId) {
        diaryRepository.delete(diaryId);
    }
}
