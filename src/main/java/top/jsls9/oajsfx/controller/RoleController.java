package top.jsls9.oajsfx.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.service.RoleService;
import top.jsls9.oajsfx.utils.RespBean;
import top.jsls9.oajsfx.vo.RoleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/28 - 22:16
 */
@Api(tags = "角色管理")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * options  仅查询低于当前登录角色最高等级的角色
     * value    当userId不为空时赋值，查询userId的所属权限
     * @return
     */
    @ApiOperation("查询所有角色（为分配角色提供支持）")
    @GetMapping("/roleCheckboxes")
    public Object getRoleList(String userId){
        try {
            if(StringUtils.isBlank(userId)){
                return RespBean.error("查询失败，参数缺失。");
            }
            //适配amis接口
            HashMap<String, Object> resultMap = new HashMap<>();
            List<Role> roleList = roleService.getRoleListByRoleLevel();
            List<Object> objects = toLabelByRoles(roleList);
            resultMap.put("options",objects);
            if(StringUtils.isNotBlank(userId)){
                //查询userId所拥有的权限
                List<Role> rolesByUserId =roleService.getRoleByUserId(userId);
                resultMap.put("value",toLabelByRoles(rolesByUserId));
            }

            return RespBean.success("查询成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    /**
     * 处理权限集合，使得兼容amsi
     * @param roleList  权限集合
     * @return
     */
    private List<Object> toLabelByRoles(List<Role> roleList){
        List<Object> list = new ArrayList<>();
        for(Role role : roleList){
            HashMap<String,String> map=new HashMap<>();
            map.put("label",role.getDescription());
            map.put("value",role.getId());
            list.add(map);
        }
        return list;
    }

    /**
     * 分页查询所有角色
     * @param page
     * @param perPage
     * @return
     */
    @ApiOperation("查询所有角色-crudList")
    @GetMapping("/roles")
    public Object getRoleCrudList(@RequestParam Integer page, @RequestParam Integer perPage){
        try {
            PageInfo<Role> rolesByPage = roleService.getRolesByPage(page, perPage);
            Map<String, Object> map = new HashMap<>();
            map.put("count",rolesByPage.getTotal());
            map.put("rows",rolesByPage.getList());
            return RespBean.success("查询成功",map);
        }catch (Exception e){
            return RespBean.error("查询失败");
        }
    }

    @ApiOperation("增加角色")
    @PostMapping("/role")
    public Object addRole(@RequestBody RoleDTO dto){
        if(dto == null){
            return RespBean.error("参数缺失");
        }
        int i = roleService.addRole(dto);
        if(i > 0){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation("查询角色")
    @GetMapping("/role/{id}")
    public Object queryRoleById(@PathVariable("id") String rId){
        Role role = roleService.getRoleById(rId);
        return RespBean.success("查询成功",role);
    }

    @ApiOperation("修改角色")
    @PutMapping("/role/{id}")
    public Object updateRoleById(@PathVariable("id") String rId, @RequestBody RoleDTO dto){
        try {
            if(dto == null){
                return RespBean.error("参数缺失");
            }
            roleService.updateRole(dto);
            return RespBean.success("修改成功");
        }catch (Exception e){
            return RespBean.error("修改失败");
        }
    }

    @ApiOperation("删除（批量）角色")
    @DeleteMapping("/role/{id}")
    public Object updateRoleById(@PathVariable("id") String rId){
        try {
            String[] ids = rId.split(",");
            int i = roleService.delRoleById(ids);
            return RespBean.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("删除失败", e.getMessage());
        }
    }

}
