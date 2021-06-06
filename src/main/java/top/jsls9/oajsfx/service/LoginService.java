package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.User;

/**
 * @author bSu
 * @date 2021/3/14 - 2:15
 */
public interface LoginService {

    /**
     * 通过用户名查询user
     * @return
     */
    User getUserByUserName(String userName) ;

}
