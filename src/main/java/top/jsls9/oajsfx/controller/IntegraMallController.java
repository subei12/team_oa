package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.service.IntegralMallService;
import top.jsls9.oajsfx.utils.RespBean;

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

}
