package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.IntegralMall;

import java.util.List;


@Mapper
@Repository
public interface IntegralMallDao {
    int deleteByPrimaryKey(String integralMallId);

    int insert(IntegralMall record);

    int insertSelective(IntegralMall record);

    IntegralMall selectByPrimaryKey(String integralMallId);

    int updateByPrimaryKeySelective(IntegralMall record);

    int updateByPrimaryKey(IntegralMall record);

    List<IntegralMall> integralMallList();

    int updateGoodsCountByCas(IntegralMall integralMall);
}