package project.diary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.diary.repository.DiaryRepository;
import project.diary.repository.JdbcTemplateDiaryRepository;
import project.diary.repository.JdbcTemplateUserRepository;
import project.diary.repository.UserRepository;

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
}
