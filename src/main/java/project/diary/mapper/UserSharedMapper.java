package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.diary.domain.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserSharedMapper {
    void save(@Param("ownerUserId") Long ownerUserId, @Param("sharedUserId") Long sharedUserId);
    Optional<User> findUserIdByLoginId(@Param("loginId") String loginId);
    boolean isAlreadyShared(@Param("ownerUserId") Long ownerUserId, @Param("sharedUserId") Long sharedUserId);
    List<User> findSharedUsers(@Param("userId") Long userId);
    List<User> findUsersWhoSharedWithMe(@Param("userId") Long userId);
    void delete(@Param("ownerUserId") Long ownerUserId, @Param("sharedUserId") Long sharedUserId);
}
