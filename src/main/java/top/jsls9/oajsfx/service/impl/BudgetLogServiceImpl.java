package top.jsls9.oajsfx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.dao.BudgetLogDao;
import top.jsls9.oajsfx.dao.DeptDao;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.service.BudgetLogService;

/**
 * @author bSu
 * @date 2021/7/29 - 21:20
 */
@Service
public class BudgetLogServiceImpl implements BudgetLogService {

    @Autowired
    private DeptDao deptDao;
    @Autowired
    private BudgetLogDao budgetLogDao;


    @Override
    @Transactional
    public int updateDeptBudget(BudgetLog budgetLog) {
        Dept dept = new Dept();
        dept.setId(budgetLog.getDeptId());
        dept.setSource(budgetLog.getSource());
        //修改团队预算
        deptDao.updateNameById(dept);
        //插入修改日志
        return budgetLogDao.insert(budgetLog);
    }

}
