package top.jsls9.oajsfx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.dao.IntegralMallLogDao;
import top.jsls9.oajsfx.model.IntegralMall;
import top.jsls9.oajsfx.model.IntegralMallLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.IntegralMallService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.RespBean;

import javax.crypto.interfaces.PBEKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 积分商城
 * @author bSu
 * @date 2021/6/14 - 18:26
 */
@Api(tags = "积分商城处理接口")
@RestController
public class IntegraMallController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IntegralMallService integralMallService;

    @Autowired
    private UserService userService;

    @Autowired
    private IntegralMallLogDao integralMallLogDao;

    @ApiOperation("查询全部商品")
    @GetMapping("/goods")
    public RespBean goods(@RequestParam Integer page, @RequestParam Integer perPage){
        try {
            Map<String, Object> map = integralMallService.integralMallList(page, perPage);
            return RespBean.success("查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询失败",e.getMessage());
            return RespBean.error("查询失败");
        }
    }

    @ApiOperation("兑换商品")
    @PostMapping("/buy/{id}")
    @RequiresAuthentication //登录才可访问
    public RespBean buyGoods(@PathVariable("id") String id){
        //查询商品是否存在
        IntegralMall integralMall = integralMallService.selectByPrimaryKey(id);
        if(integralMall==null){
            return RespBean.error("兑换失败。");
        }
        //获得当前登录用户名
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        //查询登录用户信息
        User user = userService.getUserByUserName(principal);
        //查询当前商品是否兑换过，目前一个商品每月只允许兑换一次
        IntegralMallLog integralMallLog = new IntegralMallLog();
        integralMallLog.setUserId(user.getId());
        integralMallLog.setIntegralMallId(id);
        int i = integralMallLogDao.getBuyGoodsCount(integralMallLog);
        if(i>=1){
            return RespBean.error("当前商品本月已经兑换过了哦，下个月再来吧！");
        }
        //开始兑换
        Object o = integralMallService.buyGoosd(id, user);
        return (RespBean) o;
    }

    @ApiOperation("我的兑换记录")
    @GetMapping("/myGoods")
    @RequiresAuthentication //登录才可访问
    public RespBean myGoods(@RequestParam Integer page, @RequestParam Integer perPage){
        //获得当前登录用户名
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        //查询登录用户信息
        User user = userService.getUserByUserName(principal);
        PageHelper.startPage(page,perPage);
        List<IntegralMallLog> goodsLogsByUser = integralMallLogDao.getGoodsLogsByUser(user);
        PageInfo<IntegralMallLog> pageInfo = new PageInfo<IntegralMallLog>(goodsLogsByUser);
        Map<String,Object> map = new HashMap<>();
        List<IntegralMallLog> list = pageInfo.getList();
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return RespBean.success("查询成功",map);
    }

    /**
     * 多放一个好做权限控制
     * @param page
     * @param perPage
     * @param user
     * @return
     */
    @ApiOperation("全局兑换日志")
    @GetMapping("/goodsLogs")
    @RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    public RespBean goodsLogs(@RequestParam Integer page, @RequestParam Integer perPage,User user) throws IOException {
        if(StringUtils.isNotBlank(user.getUsername())){
            User userByUserName = userService.getUserByUserName(user.getUsername());
            user.setId(userByUserName.getId());
        }
        //查询时的state只是兑换记录的状态，用userModel来接收了而已
        PageHelper.startPage(page,perPage);
        List<IntegralMallLog> goodLogList = integralMallLogDao.getGoodLogList(user);
        PageInfo<IntegralMallLog> pageInfo = new PageInfo<IntegralMallLog>(goodLogList);
        Map<String,Object> map = new HashMap<>();
        List<IntegralMallLog> list = pageInfo.getList();
        for (IntegralMallLog integralMallLog : list){
            User user1 = userService.queryUserById(integralMallLog.getUserId());
            integralMallLog.setHlxUserId(user1.getHlxUserId());
            integralMallLog.setNick(user1.getNick());
        }
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return RespBean.success("查询成功",map);
    }

    /**
     * 超管处理兑换的接口
     * @param id
     * @param integralMallLog
     * @return
     */
    @ApiOperation("处理兑换")
    @PutMapping("/updateGoodsLog/{id}")
    @RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    public RespBean updateGoodsLog(@PathVariable("id") String id,@RequestBody IntegralMallLog integralMallLog){
        integralMallLog.setIntegralMallLogId(id);
        integralMallLog.setUpdateDate(new Date());
        //获得当前登录用户名
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        //查询登录用户信息
        User user = userService.getUserByUserName(principal);
        integralMallLog.setUpdateUserId(user.getId());
        integralMallLogDao.updateByPrimaryKeySelective(integralMallLog);
        return RespBean.success("处理成功");
    }


}
