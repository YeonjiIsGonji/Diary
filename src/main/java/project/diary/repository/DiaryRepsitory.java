package project.diary.repository;

import project.diary.domain.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryRepsitory {

    Diary save(Diary diary);
    Optional<Diary> findById(Long diaryId);
    List<Diary> findAll();
    void update(Long diaryId, Diary diary);
    void delete(Long diaryId);
}
