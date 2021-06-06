package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.service.RoleService;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bSu
 * @date 2021/5/28 - 22:16
 */
@Api(tags = "角色管理")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/role")
    public Object getRoleList(){
        try {
            //适配amis接口
            HashMap<String, Object> resultMap = new HashMap<>();
            List<Object> list = new ArrayList<>();
            List<Role> roleList = roleService.getRoleList();
            for(Role role : roleList){
                HashMap<String,String> map=new HashMap<>();
                map.put("label",role.getDescription());
                map.put("value",role.getId());
                list.add(map);
            }
            resultMap.put("options",list);
            return RespBean.success("查询成功",resultMap);
        }catch (Exception e){
            return RespBean.error("查询失败");
        }
    }



}
