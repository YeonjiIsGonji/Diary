package project.diary.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import project.diary.domain.User;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User save(User user) {
        //1. User 테이블에 저장
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("users").usingGeneratedKeyColumns("userId"); //자동 생성되는 PK 지정

        // User 데이터를 Map 형태로 변환하여 저장
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("loginId", user.getLoginId());
        parameters.put("password", user.getPassword());

        //INSERT 실행 및 자동 생성된 ID 가져오기
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setUserId(key.longValue());

        return user;
    }

    @Override
    public Optional<User> findByLoginId(String loginID) {
        String sql = "select * from users where loginId = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{loginID}, (rs, rowNum) -> {
            User findUser = new User();
            findUser.setUserId(rs.getLong("userId"));
            findUser.setLoginId(rs.getString("loginId"));
            findUser.setPassword(rs.getString("password"));
            return findUser;
        });

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByID(Long userId) {
        String sql = "select * from users where userId = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
            User findUser = new User();
            findUser.setUserId(rs.getLong("userId"));
            findUser.setLoginId(rs.getString("loginId"));
            findUser.setPassword(rs.getString("password"));
            return findUser;
        });

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getLong("userId"));
            user.setLoginId(rs.getString("loginId"));
            user.setPassword(rs.getString("password"));

            return user;
        });
        return users;
    }
}
