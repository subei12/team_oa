package top.jsls9.oajsfx.tgapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author bSu
 * @date 2021/7/21 - 20:49
 */
@RestController()
public class webHook {

    @PostMapping("/tgApi/webHook")
    public void webHook(@RequestBody Map<String,Object> object) {

        System.out.println("接收成功："+object);

    }

}
