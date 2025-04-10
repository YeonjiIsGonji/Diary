package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.diary.domain.EmotionType;

import java.util.List;

@Mapper
public interface DiaryEmotionMapper {
    void insertEmotion(@Param("diaryId") Long diaryId, @Param("emotion")EmotionType emotion);
    void deleteEmotion(@Param("diaryId") Long diaryId);
    List<EmotionType> findEmotion(@Param("diaryId") Long diaryId);
}
