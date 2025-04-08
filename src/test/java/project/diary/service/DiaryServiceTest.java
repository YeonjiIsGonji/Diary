package project.diary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.Diary;
import project.diary.domain.EmotionType;
import project.diary.domain.User;
import project.diary.repository.UserRepository;
import project.diary.repository.UserSharedRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSharedRepository userSharedRepository;

    private Long testUserId;

    @BeforeEach
    void init() {
        //테스트를 위한 공통 유저 생성
        User user = new User("testUser", "test1234");
        userRepository.save(user);
        testUserId = user.getUserId();
    }

    @Test
    @DisplayName("일기 저장")
    void saveDiaryTest() {
        //given
        Diary diary = new Diary();
        diary.setTitle("테스트 일기");
        diary.setContent("테스트 코드를 작성하였다.");
        diary.setDate(LocalDate.now());
        diary.setAuthorId(testUserId);
        diary.setEmotions(List.of(EmotionType.JOY, EmotionType.FEAR));

        //when
        Diary savedDiary = diaryService.saveDiary(diary);
        Optional<Diary> foundDiary = diaryService.findDiaryById(savedDiary.getId());

        //then
        assertThat(foundDiary).isPresent();
        assertThat(foundDiary.get().getTitle()).isEqualTo("테스트 일기");
        assertThat(foundDiary.get().getEmotions()).containsExactlyInAnyOrder(EmotionType.JOY, EmotionType.FEAR);

    }

    @Test
    @DisplayName("사용자 ID로 일기 목록 조회")
    void findDiariesByUserIdTest() {
        //given
        Diary diary1 = new Diary("테스트 제목1", "내용1", List.of(EmotionType.JOY, EmotionType.ANGER), LocalDate.now());
        Diary diary2 = new Diary("테스트 제목2", "내용2", List.of(EmotionType.FEAR, EmotionType.ANGER), LocalDate.now());
        diary1.setAuthorId(testUserId);
        diary2.setAuthorId(testUserId);
        diaryService.saveDiary(diary1);
        diaryService.saveDiary(diary2);

        //when
        List<Diary> diaries = diaryService.findDiariesByUserId(testUserId);

        //then
        assertThat(diaries).hasSize(2);
        assertThat(diaries).extracting(Diary::getTitle).containsExactlyInAnyOrder("테스트 제목1", "테스트 제목2");
    }

    @Test
    @DisplayName("사용자 ID로 일기 한 건 조회")
    void findDiaryByIdTest() {
        // given
        Diary diary = new Diary("제목", "내용", List.of(EmotionType.JOY, EmotionType.ANGER), LocalDate.now());
        diary.setAuthorId(testUserId);
        diaryService.saveDiary(diary);

        //when
        Optional<Diary> foundDiary = diaryService.findDiaryById(diary.getId());

        //then
        assertThat(foundDiary.get().getTitle()).isEqualTo("제목");
        assertThat(foundDiary.get().getEmotions()).containsExactlyInAnyOrder(EmotionType.JOY, EmotionType.ANGER);
    }

    @Test
    @DisplayName("유저가 공유받은 다이어리 조회")
    void findSharedDiaries() {
        // given: 유저 2명 생성
        User owner = new User("owner", "1234");
        userRepository.save(owner);
        User sharedUser = new User("shared", "5678");
        userRepository.save(sharedUser);

        // 일기 작성 (owner가 작성)
        Diary diary = new Diary("공유일기", "내용", List.of(EmotionType.JOY), LocalDate.now());
        diary.setAuthorId(owner.getUserId());
        diaryService.saveDiary(diary);

        // 공유 관계 설정: owner → sharedUser
        userSharedRepository.save(owner.getUserId(), sharedUser.getUserId());

        // when
        List<Diary> sharedDiaries = diaryService.findSharedDiaries(sharedUser.getUserId());

        // then
        assertThat(sharedDiaries).hasSize(1);
        assertThat(sharedDiaries.get(0).getTitle()).isEqualTo("공유일기");
        assertThat(sharedDiaries.get(0).getAuthorLoginId()).isEqualTo("owner");
    }

    @Test
    @DisplayName("일기 수정 테스트")
    void updateDiaryTest() {
        // given
        Diary diary = new Diary("수정 전", "내용", List.of(EmotionType.SADNESS), LocalDate.now());
        diary.setAuthorId(testUserId);
        Diary saved = diaryService.saveDiary(diary);

        Diary updatedDiary = new Diary("수정 후", "수정된 내용", List.of(EmotionType.JOY, EmotionType.ANGER), LocalDate.now());

        // when
        diaryService.updateDiary(saved.getId(), updatedDiary);
        Optional<Diary> result = diaryService.findDiaryById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("수정 후");
        assertThat(result.get().getEmotions()).containsExactlyInAnyOrder(EmotionType.ANGER, EmotionType.JOY);
    }

    @Test
    @DisplayName("일기 삭제 테스트")
    void deleteDiaryTest() {
        // given
        Diary diary = new Diary("제목", "내용1", List.of(EmotionType.JOY, EmotionType.ANGER), LocalDate.now());
        diary.setAuthorId(testUserId);
        Diary saved = diaryService.saveDiary(diary);

        // when
        diaryService.deleteDiary(saved.getId());

        // then
        Optional<Diary> result = diaryService.findDiaryById(saved.getId());
        assertThat(result).isEmpty();
    }
}