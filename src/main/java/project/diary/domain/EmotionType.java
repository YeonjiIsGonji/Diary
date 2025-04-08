package project.diary.domain;

public enum EmotionType {

    JOY("기쁨"), SADNESS("슬픔"), ANGER("분노"), FEAR("두려움"), DISGUST("불쾌");

    private final String description;

    EmotionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
