package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.HlxCategoryData;

import java.util.List;

@Mapper
@Repository
public interface HlxCategoryDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(HlxCategoryData record);

    int insertSelective(HlxCategoryData record);

    HlxCategoryData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HlxCategoryData record);

    int updateByPrimaryKey(HlxCategoryData record);

    List<HlxCategoryData> selectByDateAndType(String date, int type);
}