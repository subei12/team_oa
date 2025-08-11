package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.jsls9.oajsfx.dao.*;
import top.jsls9.oajsfx.enums.SysSourceLogType;
import top.jsls9.oajsfx.model.*;
import top.jsls9.oajsfx.model.dto.UserRewardDTO;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.RedisUtil;
import top.jsls9.oajsfx.utils.RespBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private UserRoleDao userRoleDao;

    @Autowired
    private BudgetLogDao budgetLogDao;

    @Autowired
    private HlxUtils hlxUtils;

    @Autowired
    private SysSourceLogDao sysSourceLogDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Map<String, Object> queryUsersByPageAndUser(Integer pageNum, Integer pageSize, User user) {
        Subject subject = SecurityUtils.getSubject();
        if(!subject.hasRole("superAdmin")){
            User userLogin = getUserLogin();//暂时改成只能查询当前登录用户所在部门(超级管理员除外)  TODO（还没有改成多级部门）
            user.setDeptId(userLogin.getDeptId());
            if(StringUtils.isBlank(user.getDeptId())){
                return null;
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        List<User> users = userDao.queryUsers(user);
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        Map<String,Object> map = new HashMap<>();
        List<User> list = pageInfo.getList();
        for (User u : list){
            //获取到角色名
            List<Role> roles = roleDao.getRoleByUserId(u.getId());
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
        int i = userDao.insertSelective(user);
        if(i <= 0){
            return 0;
        }
        //插入时默认赋予普通用户权限
        Role role = roleDao.selectByPrimaryKey("e9b7762a-d38d-11ec-8189-00163e0cb384");//先写死吧，以后换数据字典
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleDao.insert(userRole);
        return i;
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
        hlxUtils.sendSorcePlus(type,commentId,budgetLog.getText(),String.valueOf(budgetLog.getSource()), dept.getDeptKey());
        //扣除预算
        //数量要为负数，这里只用来奖励
        dept.setSource(-budgetLog.getSource());
        //必须要有，不然不知道是哪个团队的
        budgetLog.setDeptId(dept.getId());
        //修改团队预算
        deptDao.updateNameById(dept);
        //插入修改日志
        budgetLogDao.insert(budgetLog);
        //全局（各种不同赠送类型的汇总）日志
        sysSourceLogDao.insertLog(user.getHlxUserId(), budgetLog.getSource(), SysSourceLogType.TYPE3.getValue());

    }

    /**
     * 给用户赋予角色
     * @param roles 角色ids
     * @param id 被操作用户id
     */
    @Override
    public void giveRoleByRolesAndUserId(String roles, String id) {
        User userLogin = getUserLogin();
        //查询角色等级，等级数字越大权限越小；即当前操作人的最高角色等级要小于要赋予的角色
        String topLevelByUserId = roleDao.getRoleTopLevelByUserId(userLogin.getId());
        if(StringUtils.isBlank(topLevelByUserId)){
            return;
        }
        List<Role> roleByUserId = roleDao.getRoleByUserId(id);
        //先删除当前用户的所有角色，比当前用户最高权限数字大的才可以被删除。
        for(Role r : roleByUserId){
            if(Integer.valueOf(topLevelByUserId) >= r.getLevel()){
                continue;
            }
            userRoleDao.deleteByUserIdAndRId(id, r.getId());
        }
        //如果checkboxes不为空，就代表此用户被赋予角色
        String[] roleIds = roles.split(",");
        //给用户赋予角色
        for(String roleId : roleIds){
            Role role = roleDao.selectByPrimaryKey(roleId);
            if(role == null){
                continue;
            }
            if(Integer.valueOf(topLevelByUserId) >= role.getLevel()){
                //操作用户的最高等级比当前角色的等级大或者等，表示没有此角色权限
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(id);
            userRoleDao.insert(userRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String withdrawal(String hlxuserId, Integer count) throws IOException {
        //查询本账号剩余葫芦
        User userLogin = getUserLogin();
        User user = userDao.selectByPrimaryKey(userLogin.getId());
        //判断剩余葫芦是否够本次提现
        if(user.getGourd() < count){
            return "账户剩余葫芦不足，请刷新/检查后重试。";
        }
        //扣除葫芦
        user.setGourd(  -count );
        userDao.updateGourdByHlxUserId(user);
        //赠送葫芦
        String commentId = HlxUtils.getNewCommentId(hlxuserId);//最新的回复id
        hlxUtils.sendSorcePlus("2", commentId, "OA账户余额提取", String.valueOf(count));
        //统计全局日志
        sysSourceLogDao.insertLog(user.getHlxUserId(), count, SysSourceLogType.TYPE2.getValue());
        return null;
    }

    /**
     * 查询各团队一个月内没发帖的人员
     * 无效分页
     * @param page
     * @param perPage
     * @param user
     * @return
     */
    @Override
    public Object getUsersPosSituationtByDeptId(Integer page, Integer perPage, User user) {

        Subject subject = SecurityUtils.getSubject();
        if(!subject.hasRole("superAdmin")){
            User userLogin = getUserLogin();//暂时改成只能查询当前登录用户所在部门(超级管理员除外)  TODO（还没有改成多级部门）
            user.setDeptId(userLogin.getDeptId());
        }
        if(StringUtils.isBlank(user.getDeptId())){
            return null;
        }

        Object o = redisUtil.get(user.getDeptId());
        return o;
    }

    @Override
    public RespBean batchUpdateUserReward(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<UserRewardDTO> rewards = new ArrayList<>();
        int totalSource = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            String hlxUserId = getStringCellValue(row.getCell(0));
            if (StringUtils.isBlank(hlxUserId)) {
                logger.info("第一列为空，解析结束");
                break;
            }

            String nickName = getStringCellValue(row.getCell(1));
            Integer source = getIntegerCellValue(row.getCell(2));
            String reason = getStringCellValue(row.getCell(3));
            logger.info("解析到第 " + i + "行，内容为：" + hlxUserId + "," + nickName + "," + source + "," + reason);


            UserRewardDTO reward = new UserRewardDTO();
            reward.setHlxUserId(hlxUserId);
            reward.setNickName(nickName);
            reward.setSource(source);
            reward.setReason(reason);

            // Validate data
            if (reward.getSource() == null || reward.getSource() <= 0) {
                reward.setError("数据缺失或无效.");
            } else {
                totalSource += reward.getSource();
            }
            rewards.add(reward);
        }

        // 校验预算
        User userLogin = getUserLogin();
        Dept dept = deptDao.selectByPrimaryKey(userLogin.getDeptId());
        if (dept.getSource() < totalSource) {
            return RespBean.error("预算不足.");
        }

        List<String> errors = new ArrayList<>();
        for (UserRewardDTO reward : rewards) {
            if (reward.getError() != null) {
                errors.add("用户ID: " + reward.getHlxUserId() + ", 错误: " + reward.getError());
                continue;
            }

            try {
                BudgetLog budgetLog = new BudgetLog();
                User user = queryUserByHlxUserId(reward.getHlxUserId());
                if (user == null) {
                    errors.add("用户ID: " + reward.getHlxUserId() + ", 错误: 用户不存在.");
                    continue;
                }
                budgetLog.setUserId(user.getId());
                budgetLog.setSource(reward.getSource());
                budgetLog.setText(reward.getReason());
                budgetLog.setCreateUserId(userLogin.getId());
                updateUserRewardByUserId(budgetLog);
            } catch (Exception e) {
                errors.add("用户ID: " + reward.getHlxUserId() + ", 错误: " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            return RespBean.error("批量发放奖励失败.", errors);
        }

        return RespBean.success("批量发放奖励成功.");
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private Integer getIntegerCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                if(StringUtils.isNotBlank(cell.getStringCellValue())){
                    return Integer.parseInt(cell.getStringCellValue());
                }
                return null;
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                return null;
        }
    }
}
