package project.diary.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import project.diary.domain.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateUserSharedRepository implements UserSharedRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserSharedRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 다이어리를 특정 사용자에게 공유함 (user_shared 테이블에 추가)
     * @param ownerUserId 로그인한 다이어리 소유자
     * @param sharedUserId 공유 대상자
     */
    @Override
    public void save(Long ownerUserId, Long sharedUserId) {
        String sql = "insert into user_shared (owner_user_id, shared_user_id) values (?, ?)";
        jdbcTemplate.update(sql, ownerUserId, sharedUserId);
    }

    /**
     * loginId를 입력하면 userId로 변환
     */
    @Override
    public Optional<User> findUserIdByLoginId(String loginId) {
        String sql = "select userId, loginId from users where loginId = ?";
        return jdbcTemplate.query(sql, userRowMapper(), loginId).stream().findFirst();
    }

    @Override
    public boolean isAlreadyShared(Long ownerUserId, Long sharedUserId) {
        String sql = "select count(*) from user_shared where owner_user_id = ? and shared_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, ownerUserId, sharedUserId);
        return count != null & count > 0;
    }

    /**
     * 나의 일기를 볼 수 있는 친구 리스트
     */
    @Override
    public List<User> findSharedUsers(Long userId) {
        String sql = "select users.userId, users.loginId from users join user_shared on users.userId = user_shared.shared_user_id where user_shared.owner_user_id = ?";

        return jdbcTemplate.query(sql, userRowMapper(), userId);
    }

    /**
     * 나에게 일기를 공유한 친구 리스트
     */
    @Override
    public List<User> findUsersWhoSharedWithMe(Long userId) {
        String sql ="select users.userId, users.loginId from users join user_shared on users.userId = user_shared.owner_user_id where user_shared.shared_user_id = ?";

        return jdbcTemplate.query(sql, userRowMapper(), userId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User sharedUser = new User();
            sharedUser.setUserId(rs.getLong("userId"));
            sharedUser.setLoginId(rs.getString("loginId"));
            return sharedUser;
        };
    }

    @Override
    public void delete(Long ownerUserId, Long sharedUserId) {
        String sql = "delete from user_shared where owner_user_id = ? and shared_user_id = ?";
        jdbcTemplate.update(sql, ownerUserId, sharedUserId);
    }
}
