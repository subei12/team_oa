package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.IntegralMall;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/6/14 - 18:31
 */
public interface IntegralMallService {

    Map<String,Object> integralMallList(Integer pageNum, Integer pageSize);

}
