package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.Map;

/**
 * @author bSu
 * @date 2021/8/22 - 10:03
 */
@Api(tags = "QqMsgWebHook接口")
@RestController
public class QqMsgWebHookController {

    @PostMapping("/qq/webhook")
    public String qqWebHook(@RequestBody Map map){
        System.out.println(map.toString());
        System.out.println(JSON.toJSON(map));
        String str = "{\"command\":\"sendFriendMessage\",\n" +
                "\"content\":{\n" +
                "  \"target\":2622798046,\n" +
                "  \"messageChain\":[\n" +
                "    { \"type\":\"Plain\", \"text\":\"hello\\n\" },\n" +
                "    { \"type\":\"Plain\", \"text\":\"world\" },\n" +
                "\t{ \"type\":\"Image\", \"url\":\"https://i0.hdslb.com/bfs/album/67fc4e6b417d9c68ef98ba71d5e79505bbad97a1.png\" }\n" +
                "  ]\n" +
                "}\n" +
                "}";
        return null;
    }


}
