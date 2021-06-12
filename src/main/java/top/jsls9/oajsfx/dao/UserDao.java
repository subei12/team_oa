package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.User;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    int deleteByPrimaryKey(String[] ids);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 通过用户名查询user
     * @return
     */
    User getUserByUserName(String userName) ;

    /**
     * 查询所有用户
     * @return
     */
    List<User> getUsers();

    int getCountByUserName(String userName);

    int getUserCount(Integer page, Integer perPage);

    List<User> queryUsers(User user);

    int updateUserById(User user);

    int updateUserPwdByUserId(User user);

    void updateIntegralByHlxUserId(User user);
}