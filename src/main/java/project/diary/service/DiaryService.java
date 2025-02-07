package project.diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.diary.domain.Diary;
import project.diary.repository.DiaryRepsitory;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepsitory diaryRepsitory;

    @Autowired
    public DiaryService(DiaryRepsitory diaryRepsitory) {
        this.diaryRepsitory = diaryRepsitory;
    }

    public List<Diary> findAllDiaries() {
        return diaryRepsitory.findAll();
    }

    public Optional<Diary> findDiaryById(Long diaryId) {
        return diaryRepsitory.findById(diaryId);
    }

    public Diary saveDiary(Diary diary) {
        return diaryRepsitory.save(diary);
    }

    public void updateDiary(Long diaryId, Diary diary) {
        diaryRepsitory.update(diaryId, diary);
    }

    public void deleteDiary(Long diaryId) {
        diaryRepsitory.delete(diaryId);
    }
}
