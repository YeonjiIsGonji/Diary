package project.diary.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import project.diary.domain.ShareRequest;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTemplateShareRequestRepository implements ShareRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateShareRequestRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 친구 요청 저장
     */
    @Override
    public void saveFriendRequest(Long senderId, Long receiverId) {
        String checkSql = "select count(*) from friend_requests where sender_id = ? and receiver_id = ? and status = 'REJECTED'";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, senderId, receiverId);

        if (count != null && count > 0) {
            // 기존에 거절했던 친구 요청이 존재, 다시 PENDING으로 변경
            String updateSql = "update friend_requests set status = 'PENDING' where sender_id = ? and receiver_id = ?";
            jdbcTemplate.update(updateSql, senderId, receiverId);
        } else {
            String insertSql = "insert into friend_requests (sender_id, receiver_id, status) values (?, ?, 'PENDING')";
            jdbcTemplate.update(insertSql, senderId, receiverId);
        }
    }

    /**
     * 친구 요청이 존재하는지 확인
     */
    @Override
    public boolean isRequestExists(Long senderId, Long receiverId) {
        String sql = "select count(*) from friend_requests where sender_id = ? and receiver_id = ? and status = 'PENDING'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, senderId, receiverId);
        return count != null && count > 0;
    }

    /**
     * 받은 친구 요청 조회
     */
    @Override
    public List<ShareRequest> findPendingRequestsToMe(Long receiverId) {
        String sql = "select fr.id, fr.sender_id, u.loginId, fr.receiver_id, fr.status " +
                "from friend_requests fr " +
                "join users u on fr.sender_id = u.userId " +
                "where fr.receiver_id = ? and fr.status = 'PENDING'";
        return jdbcTemplate.query(sql, friendRequestToMeRowMapper(), receiverId);
    }

    /**
     * 보낸 친구 요청 조회
     */
    @Override
    public List<ShareRequest> findPendingRequestsFromMe(Long senderId) {
        String sql = "select fr.id, fr.sender_id, fr.receiver_id, u.loginId, fr.status " +
                "from friend_requests fr " +
                "join users u on fr.receiver_id = u.userId " +
                "where fr.sender_id = ? and fr.status = 'PENDING'";
        return jdbcTemplate.query(sql, friendRequestFromMeRowMapper(), senderId);
    }

    /**
     * 친구 요청 수락
     */
    @Override
    public void acceptRequest(Long requestId) {
        String sql = "update friend_requests set status = 'ACCEPTED' where id = ?";
        jdbcTemplate.update(sql, requestId);
    }

    /**
     * 친구 요청 거절
     */
    @Override
    public void rejectRequest(Long requestId) {
        String sql = "update friend_requests set status = 'REJECTED' where id = ?";
        jdbcTemplate.update(sql, requestId);
    }

    private RowMapper<ShareRequest> friendRequestToMeRowMapper() {
        return (rs, rowNum) -> {
            ShareRequest request = new ShareRequest();
            request.setFriendRequestId(rs.getLong("id"));
            request.setSenderId(rs.getLong("sender_id"));
            request.setReceiverId(rs.getLong("receiver_id"));
            request.setStatus(rs.getString("status"));
            request.setSenderLoginId(rs.getString("loginId"));
            return request;
        };
    }

    private RowMapper<ShareRequest> friendRequestFromMeRowMapper() {
        return (rs, rowNum) -> {
            ShareRequest request = new ShareRequest();
            request.setFriendRequestId(rs.getLong("id"));
            request.setSenderId(rs.getLong("sender_id"));
            request.setReceiverId(rs.getLong("receiver_id"));
            request.setStatus(rs.getString("status"));
            request.setReceiverLoginId(rs.getString("loginId"));
            return request;
        };
    }
}
