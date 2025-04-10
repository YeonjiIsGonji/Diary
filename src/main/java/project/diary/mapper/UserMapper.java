package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.diary.domain.User;

import java.util.List;

@Mapper
public interface UserMapper {
    void save(User user);
    User findByLoginId(@Param("loginId") String loginId);
    User findByUserId(@Param("userId") Long userId);
    List<User> findAll();
}
