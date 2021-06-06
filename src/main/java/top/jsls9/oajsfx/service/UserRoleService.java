package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.UserRole;

/**
 * @author bSu
 * @date 2021/5/28 - 22:56
 */
public interface UserRoleService {


    void insert(UserRole userRole);

    void deleteByUserId(String id);

}
