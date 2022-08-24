package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.SysSourceLogDao;
import top.jsls9.oajsfx.model.SysSourceLog;
import top.jsls9.oajsfx.service.SysSourceLogService;
import top.jsls9.oajsfx.vo.SysSourceLogVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2022/8/23 - 20:43
 */
@Service
public class SysSourceLogServiceImpl implements SysSourceLogService {

    @Autowired
    private SysSourceLogDao sysSourceLogDao;

    @Override
    public Map<String, Object> queryListSourceLog(Integer pageNum, Integer pageSize, SysSourceLogVo vo) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysSourceLog> list =  sysSourceLogDao.queryListSourceLog(vo);
        PageInfo<SysSourceLog> pageInfo = new PageInfo<>(list);

        Map<String,Object> map = new HashMap<>();
        map.put("count", pageInfo.getTotal());
        map.put("rows", list);
        map.put("totalSource", sysSourceLogDao.queryTotal(vo));
        return map;
    }

}
