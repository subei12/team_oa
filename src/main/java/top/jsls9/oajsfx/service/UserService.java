package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.User;

import java.io.IOException;
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

    void updateIntegral(User user);

    /**公用方法
     * 获取登录用户信息
     * @return
     */
    User getUserLogin();

    /**
     * 给用户方法自定义奖励数量
     * @param budgetLog
     */
    void updateUserRewardByUserId(BudgetLog budgetLog) throws IOException;

}
