package project.diary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.diary.mapper.*;
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
    public UserSharedRepository userSharedRepository(UserSharedMapper userSharedMapper) {
//        return new JdbcTemplateUserSharedRepository(dataSource);
        return new MyBatisUserSharedRepository(userSharedMapper);
    }

    @Bean
    public ShareRequestRepository shareRequestRepository(ShareRequestMapper shareRequestMapper) {
//        return new JdbcTemplateShareRequestRepository(dataSource);
        return new MyBatisShareRequestRepository(shareRequestMapper);
    }
}
