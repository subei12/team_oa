package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.UserService;

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

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


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
    public User queryUserById(String id) {
        return userDao.selectByPrimaryKey(id);
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

}
