package project.diary.repository;

import project.diary.domain.Diary;

import java.util.*;

public class MemoryDiaryRepository {
    private static long sequence = 0L;
    private static final Map<Long, Diary> store = new HashMap<>();

    public Diary save(Diary diary) {
        diary.setId(++sequence);
        store.put(diary.getId(), diary);
        return diary;
    }

    public List<Diary> findAll() {
        return new ArrayList<>(store.values());
    }

    public Diary findById(Long diaryId) {
        return store.get(diaryId);
    }

    public void update(Long diaryId, Diary diary) {
        Diary findDiary = store.get(diaryId);
        findDiary.setDate(diary.getDate());
        findDiary.setTitle(diary.getTitle());
        findDiary.setContent(diary.getContent());
        findDiary.setEmotions(diary.getEmotions());
    }

    public void delete(Long diaryId) {
        store.remove(diaryId);
    }

}
