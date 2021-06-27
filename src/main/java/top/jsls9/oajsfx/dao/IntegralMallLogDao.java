package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.IntegralMallLog;
import top.jsls9.oajsfx.model.User;

import java.util.List;

@Mapper
@Repository
public interface IntegralMallLogDao {
    int deleteByPrimaryKey(String integralMallLogId);

    int insert(IntegralMallLog record);

    int insertSelective(IntegralMallLog record);

    IntegralMallLog selectByPrimaryKey(String integralMallLogId);

    int updateByPrimaryKeySelective(IntegralMallLog record);

    int updateByPrimaryKey(IntegralMallLog record);

    /**
     * 查询某用户在某月兑换某商品的次数
     * @param integralMallLog
     * @return
     */
    int getBuyGoodsCount(IntegralMallLog integralMallLog);


    /**
     * 查询用户所有的兑换记录
     * @param user
     * @return
     */
    List<IntegralMallLog> getGoodsLogsByUser(User user);

    /**
     * 用于超管使用，多条件查询兑换记录
     * @param user
     * @return
     */
    List<IntegralMallLog> getGoodLogList(User user);
}