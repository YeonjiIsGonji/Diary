package project.diary.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Diary {

    private Long id;

    private String title;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //format 안하면 th:value 미작동함
    private LocalDate date;

    private List<EmotionType> emotions;

    public Diary() {
        this.emotions = new ArrayList<>();
    }

    public Diary(String title, String content, List<EmotionType> emotions, LocalDate date) {
        this.title = title;
        this.content = content;
        this.emotions = emotions;
        this.date = date;
    }
}
