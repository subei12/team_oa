package top.jsls9.oajsfx.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.SysLog;

import java.util.List;

@Mapper
@Repository
public interface SysLogDao {
    int save(SysLog sysLog);


    SysLog getOne(int id);

    List<SysLog> getList(@Param("from") Integer from, @Param("pageSize") Integer pageSize, @Param("sysLog") SysLog sysLog);

    int count(SysLog sysLog);

    List<SysLog> getAll();
}
