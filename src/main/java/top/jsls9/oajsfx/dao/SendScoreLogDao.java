package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.SendScoreLog;

@Mapper
@Repository
public interface SendScoreLogDao {
    int deleteByPrimaryKey(String sendScoreLogId);

    int insert(SendScoreLog record);

    int insertSelective(SendScoreLog record);

    SendScoreLog selectByPrimaryKey(String sendScoreLogId);

    int updateByPrimaryKeySelective(SendScoreLog record);

    int updateByPrimaryKey(SendScoreLog record);
}