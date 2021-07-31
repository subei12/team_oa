package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.BudgetLog;
@Mapper
@Repository
public interface BudgetLogDao {
    int deleteByPrimaryKey(String budgetLogId);

    int insert(BudgetLog record);

    int insertSelective(BudgetLog record);

    BudgetLog selectByPrimaryKey(String budgetLogId);

    int updateByPrimaryKeySelective(BudgetLog record);

    int updateByPrimaryKey(BudgetLog record);
}