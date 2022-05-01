package top.jsls9.oajsfx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.PermissionDao;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.service.PermissionService;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2022/4/20 - 16:42
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> selectAll() {
        return permissionDao.selectAll();
    }

    @Override
    public List<Permission> selectAllByUserId(String userId) {
        return permissionDao.selectAllByUserId(userId);
    }

    /**
     * 参考链接：https://blog.csdn.net/qq_42570879/article/details/90386962
     * @param permission
     * @return
     */
    @Override
    public List<Permission> selectAllByForTree(Permission permission) {
        //查询所有顶级元素
        //List<Permission> tops = permissionDao.queryListTop();
        List<Permission> permissions =permissionDao.queryListByPId(permission);
        if (permissions.size()>0){
            Permission permission1 = new Permission();
            for (int i = 0; i <permissions.size() ; i++) {
                permission1.setParentId(permissions.get(i).getId());
                List<Permission> list = selectAllByForTree(permission1);
                permissions.get(i).setChildren(list);
            }
        }
        return permissions;
    }

    @Override
    public Permission queryPermissionById(Integer id) {
        return permissionDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateById(Permission p) {
        return permissionDao.updateByPrimaryKey(p);
    }

    @Override
    public int delPermissionById(Integer id) {
        return permissionDao.deleteById(id);
    }

    @Override
    public int addPermission(Permission p) {
        return permissionDao.insert(p);
    }


}
