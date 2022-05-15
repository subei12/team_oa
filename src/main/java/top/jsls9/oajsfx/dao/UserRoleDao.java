package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.UserRole;

@Mapper
@Repository
public interface UserRoleDao {
    int deleteByPrimaryKey(String id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    void deleteByUserId(String id);

    void deleteByUserIdAndRId(@Param("id") String id, @Param("rId") String id1);
}