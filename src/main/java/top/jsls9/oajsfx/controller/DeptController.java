package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.dto.DeptUpdateDTO;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.BudgetLogService;
import top.jsls9.oajsfx.service.DeptService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 23:55
 */
@Api(tags = "部门相关接口")
@RestController
public class DeptController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetLogService budgetLogService;

    /**
     * 查询全部
     * @return
     */
    @ApiOperation("查询全部部门")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/depts")
    public Object getDepts(){
        try {
            logger.info("开始查询");
            //适配amis接口
            HashMap<String, Object> resultMap = new HashMap<>();
            List<Object> list = new ArrayList<>();
            List<Dept> WebSortList = deptService.getDepts();
            for(Dept webSort : WebSortList){
                HashMap<String,String> map=new HashMap<>();
                map.put("label",webSort.getName());
                map.put("value",webSort.getId());
                list.add(map);
            }
            resultMap.put("options",list);
            return RespBean.success("查询成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询失败"+e.getMessage());
            return RespBean.error("查询失败");
        }
    }

    /**
     * 查询全部部门预算
     * @return
     */
    @ApiOperation("查询全部部门预算")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/dept/budget")
    public RespBean queryDepts(@RequestParam Integer page, @RequestParam Integer perPage){
        try {
            Map<String, Object> deptsByPage = deptService.getDeptsByPage(page, perPage);
            return RespBean.success("查询成功",deptsByPage);
        }catch (Exception e){
            logger.error("查询所有团队预算失败：",e);
            return RespBean.error("查询失败");
        }
    }


    @ApiOperation("团队预算修改")
    //@RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    @PutMapping("/dept/{id}")
    public RespBean updateSource(@PathVariable("id") String id,@RequestBody BudgetLog budgetLog){
        try {
            if(StringUtils.isBlank(budgetLog.getText()) || budgetLog.getSource()==null){
                return RespBean.error("参数缺失，修改失败。");
            }
            //此id为团队id
            budgetLog.setDeptId(id);
            User userLogin = userService.getUserLogin();
            budgetLog.setCreateUserId(userLogin.getId());
            //防止出现用户id
            budgetLog.setUserId("");
            //其实这个接口不应该写在这个service里面的
            int i = budgetLogService.updateDeptBudget(budgetLog);
            return RespBean.success("团队预算修改成功。");
        }catch (Exception e){
            logger.error("团队预算修改失败：",e);
            return RespBean.error("团队预算修改失败。");
        }
    }

    @ApiOperation("团队奖励key配置")
    @RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    @PutMapping("/dept/key/{id}")
    public RespBean updateDeptKey(@PathVariable("id") String id, @RequestBody DeptUpdateDTO dto){
        try {
            Dept dept = new Dept();
            dept.setId(id);
            dept.setDeptKey(dto.getKey());
            if (StringUtils.isBlank(dto.getKey())) {
                // 不传默认置空
                dept.setDeptKey("");
            }
            deptService.updateDeptById(dept);
            return RespBean.success("修改成功。");
        }catch (Exception e){
            logger.error("团队信息修改失败：",e);
            return RespBean.error("修改失败。");
        }
    }

    @ApiOperation("新增部门")
    @PostMapping("/dept")
    public RespBean addDept(@RequestBody Dept dept) {
        try {
            if (StringUtils.isBlank(dept.getName())) {
                return RespBean.error("部门名称不能为空");
            }
            // 设置创建时间、状态等默认值
            dept.setCreateDate(new java.util.Date());
            dept.setState(0); // 默认正常
            // 设置创建用户id
            User userLogin = userService.getUserLogin();
            dept.setCreateUserId(userLogin.getUsername());
            // 设置结算状态，默认不可结算
            if (dept.getSettlementState() == null) {
                dept.setSettlementState(1); // 1-不可结算
            }
            int i = deptService.insertWebSort(dept);
            if (i > 0) {
                return RespBean.success("新增部门成功");
            } else {
                return RespBean.error("新增部门失败");
            }
        } catch (Exception e) {
            logger.error("新增部门失败：", e);
            return RespBean.error("新增部门失败");
        }
    }

    @ApiOperation("批量删除部门")
    @DeleteMapping("/dept/{id}")
    public RespBean delDepts(@PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) {
            return RespBean.error("参数缺失,删除失败");
        }
        try {
            String[] ids = id.split(",");
            deptService.delDeptById(ids);
            return RespBean.success("删除成功");
        } catch (Exception e) {
            logger.error("删除部门失败", e);
            return RespBean.error("删除失败");
        }
    }

}
