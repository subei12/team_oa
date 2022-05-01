package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.service.PermissionService;
import top.jsls9.oajsfx.utils.RespBean;

import javax.jnlp.PersistenceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 功能权限（URL-Permission）配置
 * @author bSu
 * @date 2022/4/23 - 18:31
 */
@Api(tags = "功能权限（URL-Permission）配置")
@RestController
public class PermissionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有权限配置
     * @param page
     * @param perPage
     * @return
     */
    @ApiOperation("查询所有权限配置")
    @GetMapping("/permissions")
    public RespBean getPermissions(@RequestParam Integer page, @RequestParam Integer perPage){
        try {
            List<Permission> permissions = permissionService.selectAllByForTree(new Permission());
            Map<String, Object> map = new HashMap<>();
            map.put("count",1);
            map.put("rows",permissions);
            //淦，原来不按照amis的例子返回也能正常显示
            return RespBean.success("查询成功",permissions);
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    @ApiOperation("查询")
    @GetMapping("/permission/{id}")
    public RespBean getPermission(@PathVariable("id") Integer id){
        try {
            Permission permission = permissionService.queryPermissionById(id);
            return RespBean.success("查询成功",permission);
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    @ApiOperation("修改")
    @PutMapping("/permission/{id}")
    public RespBean updatePermission(@PathVariable("id") Integer id,@RequestBody Permission p){
        try {
            if(id != p.getId()){
                return RespBean.error("修改失败，参数不一致。");
            }
            permissionService.updateById(p);
            return RespBean.success("修改成功");
        }catch (Exception e){
            logger.error("修改成功",e.getMessage());
            e.printStackTrace();
            return RespBean.error("修改失败");
        }
    }

    @ApiOperation("删除")
    @DeleteMapping("/permission/{id}")
    public RespBean delPermission(@PathVariable("id") Integer id){
        int i = permissionService.delPermissionById(id);
        if(i == 1){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation("新增")
    @PostMapping("/permission")
    public RespBean addPermission(@RequestBody Permission p){
        int i = permissionService.addPermission(p);
        if(i == 1){
            return RespBean.success("新增成功");
        }
        return RespBean.error("新增失败");
    }


}
