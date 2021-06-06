package top.jsls9.oajsfx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.service.RoleService;

import java.util.List;

/**
 * @author bSu
 * @date 2021/5/28 - 22:41
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }

}
