package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.DeptDao;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.service.DeptService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 23:58
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public int insertWebSort(Dept dept) {
        return deptDao.insert(dept);
    }

    @Override
    public int updateNameById(Dept webSort) {
        return deptDao.updateNameById(webSort);
    }

    @Override
    public int delDeptById(String[] ids) {
        return deptDao.deleteByPrimaryKey(ids);
    }

    @Override
    public Map<String,Object> getDeptsByPage(Integer page, Integer perPage) {
        PageHelper.startPage(page,perPage);
        List<Dept> webSortList = deptDao.getDepts();
        PageInfo<Dept> pageInfo = new PageInfo<Dept>(webSortList);
        Map<String,Object> map = new HashMap<>();
        map.put("count",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }

    @Override
    public List<Dept> getDepts() {
        return deptDao.getDepts();
    }

}
