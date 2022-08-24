package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.jsr303Mode.User;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.service.SysSourceLogService;
import top.jsls9.oajsfx.utils.RespBean;
import top.jsls9.oajsfx.vo.SysSourceLogVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2022/8/23 - 20:40
 */
@Api(tags = "奖励日志接口")
@RestController
public class SysSourceLogController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysSourceLogService sysSourceLogService;

    @ApiOperation("按条件查询奖励日志")
    @GetMapping("/sysSourceLogs")
    public RespBean getPermissions(@RequestParam Integer page, @RequestParam Integer perPage, SysSourceLogVo vo){
        try {
            //SysSourceLogVo vo = new SysSourceLogVo();
            Map<String, Object> map = sysSourceLogService.queryListSourceLog(page, perPage, vo);
            return RespBean.success("查询成功",map);
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

}
