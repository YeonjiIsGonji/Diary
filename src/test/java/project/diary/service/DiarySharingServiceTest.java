package project.diary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.ShareRequest;
import project.diary.domain.User;
import project.diary.repository.ShareRequestRepository;
import project.diary.repository.UserRepository;
import project.diary.repository.UserSharedRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class DiarySharingServiceTest {

    @Autowired
    private DiarySharingService diarySharingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareRequestRepository shareRequestRepository;

    @Autowired
    private UserSharedRepository userSharedRepository;

    private Long userAId;
    private Long userBId;

    @BeforeEach
    void init() {
        User userA = new User("userA", "userA");
        User userB = new User("userB", "userB");

        userRepository.save(userA);
        userRepository.save(userB);

        userAId = userA.getUserId();
        userBId = userB.getUserId();
    }

    @Test
    @DisplayName("sendFriendRequest_친구요청_정상")
    void 친구요청_정상() {
        // 유효한 경우
        String result = diarySharingService.sendFriendRequest(userAId, "userB");
        assertThat(result).isEqualTo("초대 요청이 전송되었습니다.");

    }

    @Test
    @DisplayName("sendFriendRequest_유효하지 않은 친구요청_없는유저에게 요청")
    void 친구요청_없는유저() {
        String result = diarySharingService.sendFriendRequest(userBId, "nonExistent");
        assertThat(result).isEqualTo("존재하지 않는 ID입니다.");
    }

    @Test
    @DisplayName("sendFriendRequest_유효하지 않은 친구요청_자기자신에게 요청")
    void 친구요청_자기자신() {
        String result = diarySharingService.sendFriendRequest(userBId, "userB");
        assertThat(result).isEqualTo("본인에게 초대 요청을 보낼 수 없습니다.");
    }

    @Test
    @DisplayName("sendFriendRequest_유효하지 않은 친구요청_이미 요청한 친구")
    void 친구요청_중복요청() {
        diarySharingService.sendFriendRequest(userAId, "userB");
        String result = diarySharingService.sendFriendRequest(userAId, "userB");
        assertThat(result).isEqualTo("이미 초대 요청을 보냈습니다.");
    }

    @Test
    @DisplayName("sendFriendRequest_유효하지 않은 친구요청_이미 친구인 친구")
    void 친구요청_이미친구() {
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.acceptFriendRequest(requestId, userAId, userBId);

        String result = diarySharingService.sendFriendRequest(userAId, "userB");
        assertThat(result).isEqualTo("이미 초대된 사용자입니다.");
    }

    @Test
    @DisplayName("나에게 친구 요청 보낸 유저 목록 조회")
    void 받은친구요청조회() {
        //userA -> userB 친구요청
        diarySharingService.sendFriendRequest(userAId, "userB");
        //userB가 확인
        List<ShareRequest> requests = diarySharingService.getPendingRequestsToMe(userBId);
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getSenderId()).isEqualTo(userAId);
    }

    @Test
    @DisplayName("내가 보낸 친구 요청 조회")
    void 보낸친구요청조회() {
        //userA -> userB 친구요청
        diarySharingService.sendFriendRequest(userAId, "userB");
        //userA가 확인
        List<ShareRequest> requests = diarySharingService.getPendingRequestsFromMe(userAId);
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getSenderId()).isEqualTo(userAId);
    }

    @Test
    @DisplayName("친구 요청 수락")
    void 친구요청수락() {
        //userA -> userB 친구요청
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.acceptFriendRequest(requestId, userAId, userBId);

        List<User> result = diarySharingService.getSharedUsers(userAId);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userBId);
    }

    @Test
    @DisplayName("친구 요청 거절")
    void 친구요청거절() {
        //userA -> userB 친구요청
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.rejectFriendRequest(requestId);

        List<User> result = diarySharingService.getSharedUsers(userAId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("나의 일기를 볼 수 있는 유저 확인")
    void 나의공유유저확인() {
        //친구 요청 후 수락
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.acceptFriendRequest(requestId, userAId, userBId);

        List<User> users = diarySharingService.getSharedUsers(userAId);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUserId()).isEqualTo(userBId);
    }

    @Test
    @DisplayName("나에게 일기를 공유한 유저 확인")
    void 나에게공유한유저확인() {
        //친구 요청 후 수락
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.acceptFriendRequest(requestId, userAId, userBId);

        List<User> users = diarySharingService.findUsersWhoSharedWithMe(userBId);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUserId()).isEqualTo(userAId);
    }

    @Test
    @DisplayName("공유 취소하고 싶은 유저를 공유대상에서 삭제하기")
    void 공유취소() {
        //친구 요청 후 수락
        diarySharingService.sendFriendRequest(userAId, "userB");
        Long requestId = shareRequestRepository.findPendingRequestsToMe(userBId).get(0).getFriendRequestId();
        diarySharingService.acceptFriendRequest(requestId, userAId, userBId);

        diarySharingService.deleteSharedUser(userAId, userBId);
        List<User> users = diarySharingService.getSharedUsers(userAId);
        assertThat(users).isEmpty();

    }
}