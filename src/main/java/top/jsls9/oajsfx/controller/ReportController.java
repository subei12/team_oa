package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.dao.ReportConfigDao;
import top.jsls9.oajsfx.dao.ReportInfoDao;
import top.jsls9.oajsfx.model.ReportConfig;
import top.jsls9.oajsfx.model.ReportInfo;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.RespBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/8/18 - 21:32
 */
@Api(tags = "上报管理接口")
@RestController
public class ReportController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReportConfigDao reportConfigDao;

    @Autowired
    private ReportInfoDao reportInfoDao;

    @Autowired
    private UserService userService;

    /**
     * 应用上下文
     */
    @Autowired
    private ApplicationContext context;

    @ApiOperation("上报统一提交接口")
    //  @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PostMapping("/report")
    public RespBean doReport(@RequestBody Map<String,Object> map) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        try{
            if(map.isEmpty() || StringUtils.isBlank((String)map.get("processId"))){
                return RespBean.error("上报失败，参数缺失");
            }
            String processId = (String) map.get("processId");
            ReportConfig reportConfig = reportConfigDao.queryConfigByProcessId(processId);
            if( reportConfig == null ){
                return RespBean.error("上报失败，查询不到此格式。");
            }
            //表单名称
            map.put("titleName",reportConfig.getTitleName());
            Class<?> aClass = Class.forName(reportConfig.getEntityClass());
            //这个创建的类无法注入变量
            //Object o1 = aClass.newInstance();
            //更换为可以注入
            Object o1 = context.getAutowireCapableBeanFactory()
                    .createBean(aClass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            Field[] fields = aClass.getDeclaredFields();//获取本类的所有属性
            Field[] superFields = aClass.getSuperclass().getDeclaredFields();//获取父类的所有属性
            for(Field field : fields){
                int modifiers = field.getModifiers();//获取字段的修饰符
                if(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)){
                    continue;
                }
                field.setAccessible(true);
                field.set(o1,map.get(field.getName()));
            }
            for(Field field : superFields){
                int modifiers = field.getModifiers();//获取字段的修饰符
                if(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)){
                    continue;
                }
                field.setAccessible(true);
                field.set(o1,map.get(field.getName()));
            }

            log.info(JSON.toJSONString(o1));
            //掉用上报方法，没有此方法会抛异常哦。
            Method doReport = o1.getClass().getMethod("doReport");
            //俺的方法其实没有返回值
            Object bean = context.getBean(o1.getClass());
            BeanUtils.copyProperties(o1,bean,new String[]{"group"});
            Object invoke = doReport.invoke(bean,null);
            //保存reportInfo
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setCreateUserId(userService.getUserLogin().getId());
            reportInfo.setCreateDate(new Date());
            reportInfo.setProcessId(reportConfig.getProcessId());
            reportInfo.setReportContent(JSON.toJSONBytes(o1));
            reportInfo.setState(0);
            reportInfoDao.insert(reportInfo);
            return RespBean.success("上报成功，请耐心等待处理。");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return RespBean.error("上报失败",e.getMessage());
        }
    }

}
