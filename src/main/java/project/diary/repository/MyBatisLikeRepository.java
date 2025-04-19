package project.diary.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.diary.mapper.LikeMapper;

@Repository
@RequiredArgsConstructor
public class MyBatisLikeRepository implements LikeRepository{

    private final LikeMapper likeMapper;

    @Override
    public void addLike(Long diaryId, Long userId) {
        likeMapper.insertLike(diaryId, userId);
    }

    @Override
    public void removeLike(Long diaryId, Long userId) {
        likeMapper.deleteLike(diaryId, userId);
    }

    @Override
    public boolean isLiked(Long diaryId, Long userId) {
        return likeMapper.isLiked(diaryId, userId);
    }

    @Override
    public int countLikes(Long diaryId) {
        return likeMapper.countLikes(diaryId);
    }
}
