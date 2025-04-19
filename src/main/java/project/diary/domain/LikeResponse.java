package project.diary.domain;

public class LikeResponse {

    private boolean liked;
    private int count;

    public LikeResponse(boolean liked, int count) {
        this.liked = liked;
        this.count = count;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getCount() {
        return count;
    }
}
