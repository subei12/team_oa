package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.vo.SysSourceLogVo;

import java.util.Map;

/**
 * @author bSu
 * @date 2022/8/23 - 20:41
 */
public interface SysSourceLogService {

    /**
     * 按条件查询奖励日志
     * @param pageNum
     * @param pageSize
     * @param log
     * @return
     */
    Map<String, Object> queryListSourceLog(Integer pageNum, Integer pageSize, SysSourceLogVo log);

}
