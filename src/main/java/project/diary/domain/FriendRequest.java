package project.diary.domain;

import lombok.Data;

@Data
public class FriendRequest {
    private Long friendRequestId;
    private Long senderId;
    private String senderLoginId;
    private Long receiverId;
    private String receiverLoginId;
    private String status; //요청 상태(PENDING, ACCEPTED, REJECTED)

    public FriendRequest() {
    }

    public FriendRequest(Long senderId, String senderLoginId, Long receiverId, String receiverLoginId, String status) {
        this.senderId = senderId;
        this.senderLoginId = senderLoginId;
        this.receiverId = receiverId;
        this.receiverLoginId = receiverLoginId;
        this.status = status;
    }
}
