package top.jsls9.oajsfx.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.dao.SysLogDao;
import top.jsls9.oajsfx.model.SysLog;
import top.jsls9.oajsfx.service.SysLogService;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Transactional
    @Override
    public int saveSysLog(SysLog sysLog) {
        return sysLogDao.save(sysLog);
    }

    @Override
    public SysLog getOneSysLog(int id) {
        return sysLogDao.getOne(id);
    }

    @Override
    public List<SysLog> getListSysLog(Integer currentPage, Integer pageSize, SysLog sysLog) {
        return sysLogDao.getList((currentPage-1)*pageSize,pageSize,sysLog);
    }

    @Override
    public int countSysLog(SysLog sysLog) {
        return sysLogDao.count(sysLog);
    }

    @Override
    public List<SysLog> getAllSysLog() {
        return sysLogDao.getAll();
    }
}
