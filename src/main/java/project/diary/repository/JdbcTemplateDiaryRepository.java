package project.diary.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import project.diary.domain.Diary;
import project.diary.domain.EmotionType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateDiaryRepository implements DiaryRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateDiaryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Diary save(Diary diary) {
        // 1. diary 테이블에 저장
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("diary").usingGeneratedKeyColumns("id");

        //Diary 데이터를 Map 형태로 변환하여 저장
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", diary.getTitle());
        parameters.put("content", diary.getContent());
        parameters.put("date", diary.getDate());
        parameters.put("author_id", diary.getAuthorId());

        //INSERT 실행 및 자동 생성된 ID 가져오기
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        diary.setId(key.longValue());

        // 2. 선택된 감정을 diary_emotions 테이블에 저장
        String sql = "INSERT INTO diary_emotions (diary_id, emotion_type) VALUES (?, ?)";
        for (EmotionType emotion : diary.getEmotions()) {
            jdbcTemplate.update(sql, diary.getId(), emotion.name());
        }

        return diary;
    }

    /**
     * 1. diary 테이블에서 기본 정보 가져오기
     * 2. diary_emotions 테이블에서 해당 diary_id의 감정을 가져와 List<EmotionType>으로 변환
     */
    @Override
    public Optional<Diary> findById(Long diaryId) {
        //1. 기본 정보 가져오기
        String sql = "select * from diary where id = ?";
        List<Diary> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Diary findDiary = new Diary();
            findDiary.setId(rs.getLong("id"));
            findDiary.setTitle(rs.getString("title"));
            findDiary.setContent(rs.getString("content"));
            findDiary.setDate(rs.getDate("date").toLocalDate());
            findDiary.setAuthorId(rs.getLong("author_id"));
            return findDiary;
        }, diaryId);

        //2. 결과가 없으면 Optional.empty() 반환
        if (result.isEmpty()) {
            return Optional.empty();
        }

        //3. 감정 가져오기
        Diary diary = result.get(0);
        diary.setEmotions(findEmotionsByDiaryId(diaryId));
        return Optional.ofNullable(diary);
    }

    @Override
    public List<Diary> findDiariesByUserId(Long userId) {
        String sql = "select * from diary where author_id = ?";

        List<Diary> diaries = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Diary diary = new Diary();
            diary.setId(rs.getLong("id"));
            diary.setTitle(rs.getString("title"));
            diary.setContent(rs.getString("content"));
            diary.setDate(rs.getDate("date").toLocalDate());
            diary.setAuthorId(rs.getLong("author_id"));

            diary.setEmotions(findEmotionsByDiaryId(diary.getId()));

            return diary;
        }, userId);

        return diaries;
    }

    @Override
    public List<Diary> findSharedDiaries(Long userId) {
        String sql = "select D.*, U.loginId as authorLoginId from diary D join user_shared US on D.author_id = US.owner_user_id join users U on D.author_id = U.userId where US.shared_user_id = ?";

        List<Diary> sharedDiaries = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Diary diary = new Diary();
            diary.setId(rs.getLong("id"));
            diary.setTitle(rs.getString("title"));
            diary.setContent(rs.getString("content"));
            diary.setDate(rs.getDate("date").toLocalDate());
            diary.setAuthorId(rs.getLong("author_id"));
            diary.setAuthorLoginId(rs.getString("authorLoginId"));

            diary.setEmotions(findEmotionsByDiaryId(diary.getId()));

            return diary;
        }, userId);

        return sharedDiaries;
    }

    @Override
    public void update(Long diaryId, Diary diary) {
        // 1. diary 테이블 업데이트
        String updateDiarySql = "update diary set title = ?, content = ?, date = ? where id = ?";
        jdbcTemplate.update(updateDiarySql, diary.getTitle(), diary.getContent(), diary.getDate(), diaryId);

        // 2. 기존 감정 삭제
        String deleteEmotionSql = "delete from diary_emotions where diary_id = ?";
        jdbcTemplate.update(deleteEmotionSql, diaryId);

        // 3. 새로운 감정 삽입
        String insertEmotionSql = "insert into diary_emotions (diary_id, emotion_type) values (?, ?)";
        for (EmotionType emotion : diary.getEmotions()) {
            jdbcTemplate.update(insertEmotionSql, diaryId, emotion.name());
        }

    }

    @Override
    public void delete(Long diaryId) {
        String sql = "delete from diary where id = ?";
        jdbcTemplate.update(sql, diaryId);
    }

    private List<EmotionType> findEmotionsByDiaryId(Long diaryId) {
        String emotionSql = "select emotion_type from diary_emotions where diary_id = ?";
        // 쿼리의 결과가 여러 개의 행인 경우 각 행을 매핑하여 리스트로 반환
        List<EmotionType> emotions = jdbcTemplate.query(emotionSql,
                (rs, rowNum) -> EmotionType.valueOf(rs.getString("emotion_type")),
                diaryId);
        return emotions;
    }
}
