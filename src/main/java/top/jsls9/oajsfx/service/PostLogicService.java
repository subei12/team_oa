package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.PostLogic;

import java.util.Map;

/**
 * @author bSu
 * @date 2022/1/8 - 17:18
 */
public interface PostLogicService {

    /**
     * 分页查询所有逻辑
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> queryPostLogicByPage(Integer pageNum, Integer pageSize);

    PostLogic queryPostLogicById(Integer id);

    int update(PostLogic postLogic);

    int delete(String[] ids);

    int addPostLogicById(PostLogic postLogic);
}
