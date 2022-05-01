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
import top.jsls9.oajsfx.model.PostLogic;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.PostLogicService;
import top.jsls9.oajsfx.utils.RespBean;

/**
 * @author bSu
 * @date 2022/1/8 - 17:15
 */
@Api(tags = "帖子结算前置逻辑接口")
//@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
@RestController
public class PostLogicController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostLogicService postLogicService;

    /**
     * 查询所有前置逻辑
     * @param page
     * @param perPage
     * @return
     */
    @ApiOperation("查询所有前置逻辑")
    @GetMapping("/postLogic")
    public RespBean getPostLogic(@RequestParam Integer page, @RequestParam Integer perPage){
        try {
            return RespBean.success("查询成功",postLogicService.queryPostLogicByPage(page,perPage));
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    /**
     * 查询某一个前置逻辑
     * @return
     */
    @ApiOperation("查询某一个前置逻辑")
    @GetMapping("/postLogic/{id}")
    public RespBean getPostLogicById(@PathVariable("id") Integer id){
        try {
            if(id == null){
                return RespBean.error("参数缺失,查询失败");
            }
            return RespBean.success("查询成功",postLogicService.queryPostLogicById(id));
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    /**
     * 修改某一个前置逻辑
     * @return
     */
    @ApiOperation("修改某一个前置逻辑")
    @PutMapping("/postLogic")
    public RespBean updatePostLogicById(@RequestBody PostLogic postLogic){
        try {
            if(postLogic == null && postLogic.getId() > 0){
                return RespBean.error("参数缺失,修改失败。");
            }
            if(postLogic.getState() == null || postLogic.getState() == 1){
                postLogic.setState(0);
            }
            postLogicService.update(postLogic);
            return RespBean.success("修改成功。");
        }catch (Exception e){
            logger.error("修改失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("修改失败");
        }
    }

    /**
     * 批量删除前置逻辑
     * @return
     */
    @ApiOperation("批量前置逻辑")
    @DeleteMapping("/postLogic/{id}")
    public RespBean delPostLogicById(@PathVariable("id") String id){
        try {
            if(StringUtils.isBlank(id)){
                return RespBean.error("参数缺失,删除失败");
            }
            String[] ids = id.split(",");
            postLogicService.delete(ids);
            return RespBean.success("修改成功。");
        }catch (Exception e){
            logger.error("删除失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    /**
     * 新增前置逻辑
     * @return
     */
    @ApiOperation("新增前置逻辑")
    @PostMapping("/postLogic")
    public RespBean addPostLogicById(@RequestBody PostLogic postLogic){
        try {
            if(postLogic == null){
                return RespBean.error("参数缺失,删除失败");
            }
            postLogicService.addPostLogicById(postLogic);
            return RespBean.success("修改成功。");
        }catch (Exception e){
            logger.error("删除失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

}
