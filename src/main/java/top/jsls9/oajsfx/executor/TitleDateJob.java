package top.jsls9.oajsfx.executor;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.hlxPojo.Posts;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.DeptService;
import top.jsls9.oajsfx.service.HlxService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bSu
 * @date 2022/6/18 - 18:12
 */
@Component
public class TitleDateJob {


    @Autowired
    private DeptService deptService;

    @Autowired
    private HlxUtils hlxUtils;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HlxService hlxService;

    @Autowired
    private RedisUtil redisUtil;

    //这个id不做处理
    private final String DEPT_ID = "2f427b92-abe0-11eb-879d-1831bf447e84";
    //团队使用称号
    private final String TITLE = "民间大神";

    /**
     * 使用称号且30天内未发帖处理
     * @throws Exception
     */
    @XxlJob("TitleDateJobHandler")
    public void titleDateJobHandler() throws Exception {

        //查询所有团队
        List<Dept> depts = deptService.getDepts();

        //查询每个团队的发帖情况
        for(Dept dept : depts){
            XxlJobHelper.log("正在处理：{}团队", dept.getName());
            //查询所属团队成员
            User u = new User();
            u.setDeptId(dept.getId());
            List<User> users = userDao.queryUsers(u);

            List<User> list = new ArrayList<>();

            //查询每个成员的情况
            for(User user : users){
                //获取当前用户最新的板块帖子
                Posts post = hlxService.getPostsByUserIdQueryOne(user.getHlxUserId());
                if(post.getPostID() != 0){
                    //如果一个月内有发帖，不做统计
                    continue;
                }
                list.add(user);

            }
            //数据存入redis
            redisUtil.set(dept.getId(), list);
        }

    }

}
