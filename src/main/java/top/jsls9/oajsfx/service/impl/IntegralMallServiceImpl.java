package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.IntegralMallDao;
import top.jsls9.oajsfx.model.IntegralMall;
import top.jsls9.oajsfx.service.IntegralMallService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 取错名字了，应该叫商品表 goods，现在积分商店显得有点尴尬
 * @author bSu
 * @date 2021/6/14 - 18:33
 */
@Service
public class IntegralMallServiceImpl implements IntegralMallService {

    @Autowired
    private IntegralMallDao integralMallDao;

    @Override
    public Map<String,Object> integralMallList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<IntegralMall> integralMalls = integralMallDao.integralMallList();
        PageInfo<IntegralMall> pageInfo = new PageInfo<IntegralMall>(integralMalls);
        Map<String,Object> map = new HashMap<>();
        List<IntegralMall> list = pageInfo.getList();
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return map;
    }

}
