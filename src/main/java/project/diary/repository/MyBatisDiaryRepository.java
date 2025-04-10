package project.diary.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import project.diary.domain.Diary;
import project.diary.domain.EmotionType;
import project.diary.mapper.DiaryEmotionMapper;
import project.diary.mapper.DiaryMapper;
import project.diary.util.HtmlSanitizerUtil;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisDiaryRepository implements DiaryRepository {

    private final DiaryMapper diaryMapper;
    private final DiaryEmotionMapper diaryEmotionMapper;

    @Override
    public Diary save(Diary diary) {
        //XSS 필터링
        String sanitizedContent = HtmlSanitizerUtil.sanitize(diary.getContent());
        diary.setContent(sanitizedContent);

        diaryMapper.save(diary);

        // 감정 저장
        for (EmotionType emotion : diary.getEmotions()) {
            diaryEmotionMapper.insertEmotion(diary.getId(), emotion);
        }

        return diary;
    }

    @Override
    public Optional<Diary> findById(Long diaryId) {
        Diary diary = diaryMapper.findById(diaryId);
        if (diary != null) {
            List<EmotionType> emotions = diaryEmotionMapper.findEmotion(diaryId);
            diary.setEmotions(emotions);
        }
        return Optional.ofNullable(diary);
    }

    @Override
    public List<Diary> findDiariesByUserId(Long userId) {
        List<Diary> diaries = Optional.ofNullable(diaryMapper.findByUserId(userId))
                .orElse(List.of());

        diaries.forEach(diary -> {
            List<EmotionType> emotions = diaryEmotionMapper.findEmotion(diary.getId());
            diary.setEmotions(emotions);
        });

        return diaries;
    }

    @Override
    public List<Diary> findSharedDiaries(Long userId) {
        List<Diary> diaries = Optional.ofNullable(diaryMapper.findSharedDiaries(userId))
                .orElse(List.of());

        diaries.forEach(diary -> {
            List<EmotionType> emotions = diaryEmotionMapper.findEmotion(diary.getId());
            diary.setEmotions(emotions);
        });

        return diaries;
    }

    @Override
    public void update(Long diaryId, Diary diary) {
        //XSS 필터링
        String sanitizedContent = HtmlSanitizerUtil.sanitize(diary.getContent());
        diary.setContent(sanitizedContent);
        diary.setId(diaryId);

        diaryMapper.update(diary);

        diaryEmotionMapper.deleteEmotion(diaryId);
        for (EmotionType emotion : diary.getEmotions()) {
            diaryEmotionMapper.insertEmotion(diaryId, emotion);
        }
    }

    @Override
    public void delete(Long diaryId) {
        diaryMapper.delete(diaryId);
    }
}
