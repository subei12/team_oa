package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.model.Dept;
import top.jsls9.oajsfx.service.DeptService;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bSu
 * @date 2021/5/4 - 23:55
 */
@Api(tags = "分类接口")
@RestController
public class DeptController {

    private static Logger logger = Logger.getLogger(DeptController.class);

    @Autowired
    private DeptService deptService;

    /**
     * 查询全部
     * @return
     */
    @ApiOperation("查询全部部门")
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
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

}
