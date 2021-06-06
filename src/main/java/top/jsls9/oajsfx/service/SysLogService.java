package top.jsls9.oajsfx.service;



import top.jsls9.oajsfx.model.SysLog;

import java.util.List;

public interface SysLogService {

    int saveSysLog(SysLog sysLog);

    SysLog getOneSysLog(int id);

    List<SysLog> getListSysLog(Integer currentPage, Integer pageSize, SysLog sysLog);

    int countSysLog(SysLog sysLog);

    List<SysLog> getAllSysLog();
}
