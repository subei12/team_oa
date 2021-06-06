package top.jsls9.oajsfx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.LoginService;

/**
 * @author bSu
 * @date 2021/3/14 - 2:17
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDao usersDao;

    @Override
    public User getUserByUserName(String userName) {
        return usersDao.getUserByUserName(userName);
    }
}
