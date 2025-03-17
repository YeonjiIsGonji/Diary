package project.diary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.diary.repository.*;

import javax.sql.DataSource;

@Configuration
public class DiaryConfig {

    private final DataSource dataSource;

    public DiaryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DiaryRepository diaryRepository() {
        return new JdbcTemplateDiaryRepository(dataSource);
    }

    @Bean
    public UserRepository userRepository() {
        return new JdbcTemplateUserRepository(dataSource);
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
