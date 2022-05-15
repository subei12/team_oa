package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.Role;

import java.util.List;

@Mapper
@Repository
public interface RoleDao {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> queryRoleByUserName(String userName);

    List<Role> getRoleList();

    /**
     * 返回的是插入的条数，并把生成id赋值给role对象
     * @param role
     * @return
     */
    int addRole(Role role);

    int delRoleById(String id);

    List<Role> getRoleByUserId(String userId);

    String getRoleTopLevelByUserId(String userId);

    /**
     * 查询低于此角色等级的角色
     * @param topLevelByUserId
     * @return
     */
    List<Role> getRoleListByRoleLevel(String topLevelByUserId);
}