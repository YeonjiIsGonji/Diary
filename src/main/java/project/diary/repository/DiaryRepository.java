package project.diary.repository;

import project.diary.domain.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    Diary save(Diary diary);
    Optional<Diary> findById(Long diaryId);
    List<Diary> findDiariesByUserId(Long userId);
    void update(Long diaryId, Diary diary);
    void delete(Long diaryId);
}
