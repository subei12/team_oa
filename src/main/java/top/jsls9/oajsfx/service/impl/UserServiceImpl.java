package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.dao.BudgetLogDao;
import top.jsls9.oajsfx.dao.DeptDao;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 21:37
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private BudgetLogDao budgetLogDao;

    @Autowired
    private HlxUtils hlxUtils;

    @Override
    public Map<String, Object> queryUsersByPageAndUser(Integer pageNum, Integer pageSize, User user) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> users = userDao.queryUsers(user);
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        Map<String,Object> map = new HashMap<>();
        List<User> list = pageInfo.getList();
        for (User u : list){
            //获取到角色名
            List<Role> roles = roleDao.queryRoleByUserName(u.getUsername());
            //移除所有的null元素
            roles.removeAll(Collections.singleton(null));
            StringBuffer stringBuffer = new StringBuffer();
            for(Role role : roles){
                stringBuffer.append("、"+role.getName());
            }
            if(StringUtils.isNotBlank(stringBuffer.toString())){
                u.setRoleNames(stringBuffer.toString().substring(1));
            }
        }
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return map;
    }

    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public int getCountByUserName(String userName) {
        return userDao.getCountByUserName(userName);
    }

    @Override
    public int insetUser(User user) {
        return userDao.insertSelective(user);
    }

    @Override
    public int delUserById(String[] Ids) {
        return userDao.deleteByPrimaryKey(Ids);
    }

    @Override
    public int updateUserById(User user) {
        return userDao.updateUserById(user);
    }

    @Override
    public User queryUserById(String id) throws IOException {
        User user = userDao.selectByPrimaryKey(id);
        top.jsls9.oajsfx.hlxPojo.User user1 = hlxUtils.queryUserInfo(user.getHlxUserId());
        user.setNick(user1.getNick());
        user.setTitle(user1.getIdentityTitle());
        return user;
    }

    @Override
    public User queryUserByHlxUserId(String hlxUserId) {
        User user = new User();
        user.setHlxUserId(hlxUserId);
        List<User> users = userDao.queryUsers(user);
        if(users.size()>0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public int updateUserPwdByUserId(User user) {
        return userDao.updateUserPwdByUserId(user);
    }

    @Override
    public void updateIntegral(User user) {
        userDao.updateIntegralByHlxUserId(user);
    }

    @Override
    public void updateGourdByHlxUserId(User user) {
        userDao.updateGourdByHlxUserId(user);
    }

    @Override
    public User getUserLogin() {
        //获得当前登录用户名
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        //查询登录用户信息
        User user = getUserByUserName(principal);
        return user;
    }

    /**
     * 发放奖励
     * 加锁处理，防止超预算
     * @param budgetLog
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void updateUserRewardByUserId(BudgetLog budgetLog) throws IOException {

        //查询当前团队剩余预算
        User user = userDao.selectByPrimaryKey(budgetLog.getUserId());
        Dept dept = deptDao.selectByPrimaryKey(user.getDeptId());
        if(dept.getSource()<budgetLog.getSource()){
            throw new RuntimeException("当前预算不足，请稍后再试");
        }
        //发放奖励
        String type = "2";//赠送到评论上
        String commentId = HlxUtils.getNewCommentId(user.getHlxUserId());//最新的回复id
        if(budgetLog.getSource()<=200){
            //小于200可以直接赠送
            hlxUtils.sendSorce(type,commentId,budgetLog.getText(),String.valueOf(budgetLog.getSource()));
            logger.info("用户："+budgetLog.getUserId()+"，一次性赠送完毕！");
        }else {
            for(int i=0;(budgetLog.getSource()/200)>i;i++){
                hlxUtils.sendSorce(type,commentId,budgetLog.getText(),"200");
                logger.info("用户："+budgetLog.getUserId()+"，第"+(i+1)+"次赠送200");
            }
            if(budgetLog.getSource()%200>0){
                hlxUtils.sendSorce(type,commentId,budgetLog.getText(),String.valueOf(budgetLog.getSource()%200));
                logger.info("用户："+budgetLog.getUserId()+"，剩下一次性赠送："+budgetLog.getSource()%200);
            }
        }
        //扣除预算
        //数量要为负数，这里只用来奖励
        dept.setSource(-budgetLog.getSource());
        //必须要有，不然不知道是哪个团队的
        budgetLog.setDeptId(dept.getId());
        //修改团队预算
        deptDao.updateNameById(dept);
        //插入修改日志
        budgetLogDao.insert(budgetLog);

    }

}
