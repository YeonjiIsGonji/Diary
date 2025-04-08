package project.diary.domain;

import lombok.Data;

@Data
public class User {

    private Long userId;

    private String loginId;

    private String password;

    public User() {
    }

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
