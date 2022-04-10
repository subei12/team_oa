package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.service.impl.HlxCategoryDataServiceImpl;
import top.jsls9.oajsfx.utils.RespBean;
import top.jsls9.oajsfx.vo.HlxCategoryDataVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** 社区热度数据接口
 * @author bSu
 * @date 2022/4/10 - 14:40
 */
@Api(tags = "板块数据接口")
@RestController
public class HlxCategoryDataController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private HlxCategoryDataServiceImpl hlxCategoryDataService;

    @ApiOperation("板块数据热度数据查询")
    @GetMapping("/queryHlxCategoryDatas")
    public RespBean queryHlxCategoryDatas(String date){
        if(StringUtils.isBlank(date)){
            return RespBean.error("参数缺失");
        }
        try {
            Date parse = sdf.parse(date);
            HlxCategoryDataVo vo = hlxCategoryDataService.queryHlxCategoryDatas(sdf.format(parse));
            return RespBean.success("查询成功", vo);
        }catch (ParseException e){
            logger.error("HlxCategoryDataController.queryHlxCategoryDatas.ParseException异常", e.getMessage());
            return RespBean.error("日期格式异常，请使用 yyyy-MM-dd 格式");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("HlxCategoryDataController.queryHlxCategoryDatas异常", e.getMessage());
            return RespBean.error("查询异常，请联系管理员");
        }

    }

    @ApiOperation("板块数据热度数据查询(简洁版)")
    @GetMapping("/queryHlxCategoryDatasSimple")
    public RespBean queryHlxCategoryDatasSimple(String date){
        if(StringUtils.isBlank(date)){
            return RespBean.error("参数缺失");
        }
        try {
            Date parse = sdf.parse(date);
            Map<String, Object> map = hlxCategoryDataService.queryHlxCategoryDatasSimple(sdf.format(parse));
            return RespBean.success("查询成功", map);
        }catch (ParseException e){
            logger.error("HlxCategoryDataController.queryHlxCategoryDatasSimple.ParseException异常", e.getMessage());
            return RespBean.error("日期格式异常，请使用 yyyy-MM-dd 格式");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("HlxCategoryDataController.queryHlxCategoryDatasSimple异常", e.getMessage());
            return RespBean.error("查询异常，请联系管理员");
        }

    }


}
