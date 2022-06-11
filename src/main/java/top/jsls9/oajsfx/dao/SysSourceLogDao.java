package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.SysSourceLog;

@Mapper
@Repository
public interface SysSourceLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSourceLog record);

    int insertSelective(SysSourceLog record);

    SysSourceLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysSourceLog record);

    int updateByPrimaryKey(SysSourceLog record);

    /**
     * 赠送葫芦全局日志
     * @param hlxUserId 所得葫芦的社区id
     * @param number 所得葫芦数量
     * @param type 类型，
     * @return
     */
    int insertLog(@Param("hlxUserId") String hlxUserId,@Param("number") int number,
                  @Param("type") int type);
}