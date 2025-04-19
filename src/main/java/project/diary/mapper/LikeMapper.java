package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
    void insertLike(@Param("diaryId") Long diaryId, @Param("userId") Long userId);
    void deleteLike(@Param("diaryId") Long diaryId, @Param("userId") Long userId);
    //좋아요 여부 확인
    boolean isLiked(@Param("diaryId") Long diaryId, @Param("userId") Long userId);
    //좋아요 개수 확인
    int countLikes(@Param("diaryId") Long diaryId);
}
