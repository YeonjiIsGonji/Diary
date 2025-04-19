package project.diary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.diary.repository.MyBatisLikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final MyBatisLikeRepository likeRepository;

    /**
     * 좋아요를 토글하는 메서드
     * @return true: 좋아요 추가, false: 좋아요 취소
     */
    @Transactional
    public boolean toggleLike(Long diaryId, Long userId) {
        //기존에 좋아요 여부 확인 (true: 좋아요 추가 상태, false: 좋아요 취소 상태)
        if (likeRepository.isLiked(diaryId, userId)) {
            likeRepository.removeLike(diaryId, userId);
            return false; //좋아요 취소
        } else {
            likeRepository.addLike(diaryId, userId);
            return true; //좋아요 추가
        }
    }

    public int countLikes(Long diaryId) {
        return likeRepository.countLikes(diaryId);
    }

    //기존에 좋아요 눌렀는지 여부 확인
    public boolean isLiked(Long diaryId, Long userId) {
        return likeRepository.isLiked(diaryId, userId);
    }
}
