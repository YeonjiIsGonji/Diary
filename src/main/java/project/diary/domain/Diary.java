package project.diary.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Diary {

    @Schema(description = "Diary ID")
    private Long id;

    @Schema(description = "일기 제목")
    private String title;

    @Schema(description = "일기 내용")
    private String content;

    @Schema(description = "작성 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //format 안하면 th:value 미작동함
    private LocalDate date;

    @Schema(description = "선택된 감정 리스트")
    private List<EmotionType> emotions;

    @Schema(description = "작성자 아이디(userId)")
    private Long authorId;

    @Schema(description = "작성자 로그인 ID")
    private String authorLoginId;

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
