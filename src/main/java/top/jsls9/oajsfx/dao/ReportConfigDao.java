package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.ReportConfig;

@Mapper
@Repository
public interface ReportConfigDao {
    int deleteByPrimaryKey(String id);

    int insert(ReportConfig record);

    int insertSelective(ReportConfig record);

    ReportConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ReportConfig record);

    int updateByPrimaryKey(ReportConfig record);

    ReportConfig queryConfigByProcessId(String processId);
}