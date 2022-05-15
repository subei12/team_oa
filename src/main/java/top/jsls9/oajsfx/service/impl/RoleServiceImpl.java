package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.dao.RolePermissionDao;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.model.RolePermission;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.RoleService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.vo.RoleDTO;

import java.util.List;

/**
 * @author bSu
 * @date 2021/5/28 - 22:41
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }

    @Override
    public List<Role> getRoleListByRoleLevel() {
        //获取当前登录用户的最高角色等级
        User userLogin = userService.getUserLogin();
        String topLevelByUserId = roleDao.getRoleTopLevelByUserId(userLogin.getId());
        if(StringUtils.isBlank(topLevelByUserId)){
            return null;
        }
        List<Role> roles = roleDao.getRoleListByRoleLevel(topLevelByUserId);
        return roles;
    }

    @Override
    public PageInfo<Role> getRolesByPage(Integer page, Integer perPage) {
        PageHelper.startPage(page,perPage);
        List<Role> roleList = roleDao.getRoleList();
        PageInfo<Role> pageInfo = new PageInfo<Role>(roleList);
        return pageInfo;
    }

    @Override
    @Transactional
    public int addRole(RoleDTO dto) {
        //添加角色
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        int i = roleDao.addRole(role);
        if(i <= 0){
            return 0;
        }
        //添加角色的权限
        int num = addPermissionIdsByrId(dto.getPermissionId(), role.getId());
        return num;
    }

    @Override
    public Role getRoleById(String rId) {
        return roleDao.selectByPrimaryKey(rId);
    }

    @Override
    @Transactional
    public int updateRole(RoleDTO dto) {
        //修改角色
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        int i = roleDao.updateByPrimaryKey(role);
        //修改权限
        //1.删除此角色所有权限
        int num = rolePermissionDao.deleteByrId(role.getId());
        //2.重新批量添加角色
        addPermissionIdsByrId(dto.getPermissionId(), role.getId());
        return num;
    }


    /**
     * 给角色批量添加权限
     * @param permissionIds 权限id，多个以逗号分割
     * @param rId 角色id
     * @return
     */
    private int addPermissionIdsByrId(String permissionIds, String rId){
        String[] split = permissionIds.split(",");
        int num = 0;
        for(String pId : split){
            //插入角色-权限关系表
            RolePermission rp = new RolePermission();
            rp.setRoleId(rId);
            rp.setPermissionId(pId);
            rolePermissionDao.insert(rp);
            num++;
        }
        return num;
    }

    @Override
    public int delRoleById(String[] ids) {
        int num = 0;
        for (String rId : ids){
            int i = roleDao.delRoleById(rId);
            num = num + i;
        }
        return num;
    }

    @Override
    public List<Role> getRoleByUserId(String userId) {
        List<Role> roles = roleDao.getRoleByUserId(userId);
        return roles;
    }

}
