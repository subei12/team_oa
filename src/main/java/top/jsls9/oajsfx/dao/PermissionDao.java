package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.Permission;

import java.util.List;

@Mapper
@Repository
public interface PermissionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    List<Permission> selectAll();

    List<Permission> selectAllByUserId(String userId);

    /**
     * 查询所有顶级
     * @return
     */
    List<Permission> queryListTop();

    List<Permission> queryListByPId(Permission p);

    int deleteById(Integer id);
}