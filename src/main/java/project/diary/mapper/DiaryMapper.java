package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.diary.domain.Diary;

import java.util.List;

@Mapper
public interface DiaryMapper {
    void save(Diary diary);
    Diary findById(@Param("diaryId") Long diaryId);
    List<Diary> findByUserId(@Param("userId") Long userId);
    List<Diary> findSharedDiaries(@Param("userId") Long userId);
    void update(Diary diary);
    void delete(@Param("diaryId") Long diaryId);
}
