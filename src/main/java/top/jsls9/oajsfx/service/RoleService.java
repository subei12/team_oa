package top.jsls9.oajsfx.service;

import com.github.pagehelper.PageInfo;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.vo.RoleDTO;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/28 - 22:40
 */
public interface RoleService {

    List<Role> getRoleList();

    /**
     * 通过当前用户的最高角色等级，获取低于此等级的角色
     * @return
     */
    List<Role> getRoleListByRoleLevel();

    /**
     * 分页查询
     * @param page 第几页
     * @param perPage 页面大小
     * @return
     */
    PageInfo<Role> getRolesByPage(Integer page, Integer perPage);

    /**
     * 添加角色
     * @param role
     * @return
     */
    int addRole(RoleDTO role);

    Role getRoleById(String rId);

    int updateRole(RoleDTO dto);

    int delRoleById(String[] rId);

    List<Role> getRoleByUserId(String userId);
}
