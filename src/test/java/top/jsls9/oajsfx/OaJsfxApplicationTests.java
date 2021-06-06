package top.jsls9.oajsfx;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.HttpUtils;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
class OaJsfxApplicationTests {

    @Autowired
    private HlxUtils hlxUtils;

    @Test
    void contextLoads() throws IOException {
        String s = DigestUtils.md5DigestAsHex(("bsulike" + "su123456").getBytes());
        System.out.println(s);
    }

    @Test
    void test01(){
        String key = hlxUtils.getKey();
        System.out.println(key);
    }

    /**
     * {"code":103,"msg":"未登录","title":null,"status":0}
     * {"msg":"","iscredits":1,"status":1} 赠送成功
     * {"code":104,"msg":"贴子已锁定","title":null,"status":0}
     * @throws IOException
     */
    @Test
    void test02() throws IOException {
        //hlxUtils.sendSorce("1","47958018","测试一下。","1");
    }

    @Test
    void test03() throws IOException {
        PostsJsonRootBean postDetails = HlxUtils.getPostDetails("48190364");
        long l = new Date().getTime() - postDetails.getPost().getCreateTime() ;
        System.out.println(postDetails.getPost().getCreateTime());
        System.out.println(new Date().getTime());
        System.out.println(l);
        System.out.println(new Date().getTime() - postDetails.getPost().getCreateTime()>= 1000 * 60 * 60 * 24 *3);

    }

}
