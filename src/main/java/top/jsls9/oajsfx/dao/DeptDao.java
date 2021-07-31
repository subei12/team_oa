package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.Dept;

import java.util.List;

@Mapper
@Repository
public interface DeptDao {
    int deleteByPrimaryKey(String[] ids);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);

    List<Dept> getDepts();

    int updateNameById(Dept dept);


}