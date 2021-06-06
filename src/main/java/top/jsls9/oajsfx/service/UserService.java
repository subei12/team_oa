package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 21:37
 */
public interface UserService {

    //查询所有用户
    Map<String, Object> queryUsersByPageAndUser(Integer pageNum, Integer pageSize, User user);

    User getUserByUserName(String userName);

    int getCountByUserName(String userName);

    int insetUser(User user);

    int delUserById(String[] Ids);

    int updateUserById(User user);

    User queryUserById(String id);

    /**
     * 通过社区id查询用户
     * @param hlxUserId
     * @return
     */
    User queryUserByHlxUserId(String hlxUserId);

    int updateUserPwdByUserId(User userByUserName);
}
