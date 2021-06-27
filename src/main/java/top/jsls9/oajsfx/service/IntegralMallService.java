package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.IntegralMall;
import top.jsls9.oajsfx.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/6/14 - 18:31
 */
public interface IntegralMallService {

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String,Object> integralMallList(Integer pageNum, Integer pageSize);

    /**
     * 兑换
     * @param id
     * @param user
     * @return
     */
    Object buyGoosd(String id, User user);

    /**
     * 查询商品是否存在
     * @param id
     * @return
     */
    IntegralMall selectByPrimaryKey(String id);
}
