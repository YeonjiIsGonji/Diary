package project.diary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.diary.mapper.DiaryEmotionMapper;
import project.diary.mapper.DiaryMapper;
import project.diary.mapper.UserMapper;
import project.diary.repository.*;

import javax.sql.DataSource;

@Configuration
public class DiaryConfig {

    private final DataSource dataSource;

    public DiaryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DiaryRepository diaryRepository(DiaryMapper diaryMapper, DiaryEmotionMapper diaryEmotionMapper) {
//        return new JdbcTemplateDiaryRepository(dataSource);
        return new MyBatisDiaryRepository(diaryMapper, diaryEmotionMapper);
    }

    @Bean
    public UserRepository userRepository(UserMapper userMapper) {
//        return new JdbcTemplateUserRepository(dataSource);
        return new MyBatisUserRepository(userMapper);
    }

    @Bean
    public UserSharedRepository userSharedRepository() {
        return new JdbcTemplateUserSharedRepository(dataSource);
    }

    @Bean
    public FriendRequestRepository friendRequestRepository() {
        return new JdbcTemplateFriendRequestRepository(dataSource);
    }
}
