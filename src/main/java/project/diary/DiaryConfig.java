package project.diary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.diary.repository.DiaryRepsitory;
import project.diary.repository.JdbcTemplateDiaryRepository;

import javax.sql.DataSource;

@Configuration
public class DiaryConfig {

    private final DataSource dataSource;

    public DiaryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Bean
    public DiaryRepsitory diaryRepsitory() {
        return new JdbcTemplateDiaryRepository(dataSource);
    }
}
