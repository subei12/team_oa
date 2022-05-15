package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.vo.PermissionInputTreeVo;

import java.util.List;

/**
 * @author bSu
 * @date 2022/4/20 - 16:40
 */
public interface PermissionService {

    /**
     * 获取所有资源权限
     * @return
     */
    List<Permission> selectAll();

    /**
     * 获取用户所有权限
     * @param userId
     * @return
     */
    List<Permission> selectAllByUserId(String userId);


    /**
     * 查询所有权限，以树状菜单展示
     * @return
     */
    List<Permission> selectAllByForTree(Permission permission);

    /**
     * 通过id查询一个权限配置
     * @param integer
     * @return
     */
    Permission queryPermissionById(Integer integer);

    int updateById(Permission p);

    int delPermissionById(Integer id);

    int addPermission(Permission p);

    List<PermissionInputTreeVo> selectAllByForInputTree(PermissionInputTreeVo b);

    List<PermissionInputTreeVo> queryPermissionByUserId(String userId);

    List<PermissionInputTreeVo> queryPermissionByRoleId(String roleId);
}
