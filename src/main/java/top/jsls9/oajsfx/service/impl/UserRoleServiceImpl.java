package top.jsls9.oajsfx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.UserRoleDao;
import top.jsls9.oajsfx.model.UserRole;
import top.jsls9.oajsfx.service.UserRoleService;

/**
 * @author bSu
 * @date 2021/5/28 - 22:56
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public void insert(UserRole userRole) {
        userRoleDao.insert(userRole);
    }

    @Override
    public void deleteByUserId(String id) {
        userRoleDao.deleteByUserId(id);
    }
}

