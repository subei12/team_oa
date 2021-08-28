package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.ReportInfo;

@Mapper
@Repository
public interface ReportInfoDao {
    int deleteByPrimaryKey(Integer reportId);

    int insert(ReportInfo record);

    int insertSelective(ReportInfo record);

    ReportInfo selectByPrimaryKey(Integer reportId);

    int updateByPrimaryKeySelective(ReportInfo record);

    int updateByPrimaryKeyWithBLOBs(ReportInfo record);

    int updateByPrimaryKey(ReportInfo record);
}