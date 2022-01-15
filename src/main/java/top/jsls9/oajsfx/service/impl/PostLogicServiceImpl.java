package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.PostLogicDao;
import top.jsls9.oajsfx.model.IntegralMall;
import top.jsls9.oajsfx.model.PostLogic;
import top.jsls9.oajsfx.service.PostLogicService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2022/1/8 - 17:18
 */
@Service
public class PostLogicServiceImpl implements PostLogicService {

    @Autowired
    private PostLogicDao postLogicDao;

    /**
     * 分页查询所有结算前置逻辑
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> queryPostLogicByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<PostLogic> postLogics = postLogicDao.queryPostLogicList();
        PageInfo<PostLogic> pageInfo = new PageInfo<PostLogic>(postLogics);
        Map<String,Object> map = new HashMap<>();
        List<PostLogic> list = pageInfo.getList();
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return map;
    }

    /**
     * 通过id查询结算逻辑
     * @param id
     * @return
     */
    @Override
    public PostLogic queryPostLogicById(Integer id) {
        return postLogicDao.selectByPrimaryKey(id);
    }

    @Override
    public int update(PostLogic postLogic) {
        return postLogicDao.updateByPrimaryKeySelective(postLogic);
    }

    @Override
    public int delete(String[] ids) {
        return postLogicDao.delete(ids);
    }

    @Override
    public int addPostLogicById(PostLogic postLogic) {
        return postLogicDao.insert(postLogic);
    }


}
