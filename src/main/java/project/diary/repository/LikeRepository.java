package project.diary.repository;

public interface LikeRepository {
    void addLike(Long diaryId, Long userId);
    void removeLike(Long diaryId, Long userId);
    boolean isLiked(Long diaryId, Long userId);
    int countLikes(Long diaryId);
}
