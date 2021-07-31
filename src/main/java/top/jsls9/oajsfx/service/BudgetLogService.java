package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.BudgetLog;

/**
 * @author bSu
 * @date 2021/7/29 - 21:19
 */

public interface BudgetLogService {

    /**
     * 修改团队预算
     * @return
     */
    public int updateDeptBudget(BudgetLog budgetLog);

}
