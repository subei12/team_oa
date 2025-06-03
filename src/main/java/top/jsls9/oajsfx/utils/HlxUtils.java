package top.jsls9.oajsfx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.hlxPojo.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

/**提取的关于hlx会用常用的方法集合
 * @author bSu
 * @date 2020/11/28 - 15:53
 */
@Component
public class HlxUtils {

    private static final Logger logger = LoggerFactory.getLogger(HlxUtils.class);

    //private static final String postAdds = "";

    private static String getName(){
        return HlxUtils.class.getName();
    }

    @Value("${hlx.key.url}")
    private String getKeyUrl;

    @Value("${hlx.key.text}")
    private String key;

    @Value("${hlx.url.userInfo}")
    private String userInfoUrl;

    @Autowired
    private MailUtils mailUtils;

    /**
     * 获取key
     * @return
     */
    public String getKey(){
        return this.key;
         /*try {
             String url=getKeyUrl;
             Connection.Response response = HttpUtils.get(url);
             String body = response.body();
             JSONObject json=new JSONObject();
             GetKeyRootBean getKeyRootBean = json.parseObject(response.body(), GetKeyRootBean.class);
             if (getKeyRootBean.get_key() == null) {
                 mailUtils.sendSimpleMail("2622798046@qq.com", "OA - key失效", "OA系统key已失效，请尽快更换。");
             }
             return getKeyRootBean.get_key();
         }catch (Exception e){
             logger.error("获取key失败，",e);
             mailUtils.sendSimpleMail("2622798046@qq.com", "OA - key失效", "OA系统key已失效，请尽快更换。");
             return null;
         }*/
    }

    /**
     * 通过帖子地址获取帖子id
     * @param postAdds
     * @return
     */
    public static long getPostId(String postAdds)  {
        try {
            Document doc = Jsoup.connect(postAdds)
                    .timeout(30000)
                    .post();
            String attr = doc.getElementById("postid").attr("value");
            return Long.parseLong(attr);
        }catch (IOException e){
            logger.error(getName()+".getPostId(),io流处理异常",e);
            return 0L;
        }catch (Exception e){
            logger.error(getName()+".getPostId(),Exception异常",e);
            return 0L;
        }
    }

    /**
     * 通过帖子id获取帖子详情
     * @param postId
     */
    public static PostsJsonRootBean getPostDetails(String postId) throws IOException {
        String postJsonUrl="http://floor.huluxia.com/post/detail/IOS/1.1?platform=2&gkey=000000&app_version=4.0.0.9.1&versioncode=20141440&market_id=floor_web&post_id="+postId+"&page_no=1&page_size=20&doc=1";
        Connection.Response response = HttpUtils.get(postJsonUrl);
        String body = response.body();
        JSONObject json=new JSONObject();
        PostsJsonRootBean jsonRootBean = json.parseObject(response.body(), PostsJsonRootBean.class);
        return jsonRootBean;
    }

    /**
     * 通过帖子id和页码获取帖子回复（翻页后post值为null）
     * @param postId
     */
    public static PostsJsonRootBean getPostDetailsByPage(String postId,String pageNo) throws IOException {
        String postJsonUrl="http://floor.huluxia.com/post/detail/ANDROID/2.3?platform=2&gkey=000000&app_version=4.0.0.9.1&versioncode=20141440&market_id=floor_web&post_id="+postId+"&page_no="+pageNo+"&page_size=20&doc=1";
        Connection.Response response = HttpUtils.get(postJsonUrl);
        String body = response.body();
        JSONObject json=new JSONObject();
        PostsJsonRootBean jsonRootBean = json.parseObject(response.body(), PostsJsonRootBean.class);
        return jsonRootBean;
    }

    /** 单次赠送最高200葫芦，单次结算暂时不会超过200
     * 赠送葫芦，type=2为评论，1为帖子，
     * type为2时post_id填评论id
     * @param type
     * @param postId
     * @param text
     * @param sorce
     * @throws IOException
     */
    public void sendSorce(String type,String postId,String text,String sorce) throws IOException,RuntimeException {
        if(text.length()<5){
            text=text+".....";
        }
        String deviceCode = getDeviceCode();
        String url="https://floor.huluxia.com:443/credits/transfer/ANDROID/4.2.4?platform=2&gkey=000000&app_version=4.3.1.8&versioncode=413&market_id=tool_web&_key="+ getKey() +"&device_code="+deviceCode+"&phone_brand_type=UN";
        Map<String,String > paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        paramMap.put("score",sorce);
        paramMap.put("score_txt",text);

        Map<String, String> signMap = new HashMap<>();
        signMap.put("_key", getKey());
        signMap.put("device_code", deviceCode);
        signMap.put("post_id", postId);
        signMap.put("score_txt", text);
        signMap.put("score", sorce);
        paramMap.put("sign", toSign(signMap));

        Connection.Response post = HttpUtils.postByHlx(url, paramMap);
        logger.info(post.body());
        JSONObject json=new JSONObject();
        SendSorceJson jsonRootBean = json.parseObject(post.body(), SendSorceJson.class);
        if(jsonRootBean.getStatus()!=1){
            throw new RuntimeException(jsonRootBean.getMsg());
        }
    }

    /**赠送葫芦，不限数量（大于200会多次送）；
     * 其他引用单次的就先不改了
     * 赠送葫芦，type=2为评论，1为帖子，
     * type为2时post_id填评论id
     * @param type 2为评论，1为帖子
     * @param postId type为2时post_id填评论id
     * @param text 赠送回复文本
     * @param sorce 赠送数量
     * @throws IOException
     */
    public void sendSorcePlus(String type,String postId,String text,String sorce) throws IOException,RuntimeException {
        if(text.length()<5){
            text=text+".....";
        }
        String deviceCode = getDeviceCode();
        String url="https://floor.huluxia.com:443/credits/transfer/ANDROID/4.2.4?platform=2&gkey=000000&app_version=4.3.1.8&versioncode=413&market_id=tool_web&_key="+ getKey() +"&device_code="+deviceCode+"&phone_brand_type=UN";
        Map<String, String> paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        //paramMap.put("score",sorce);
        paramMap.put("score_txt",text);

        Map<String, String> signMap = new HashMap<>();
        signMap.put("_key", getKey());
        signMap.put("device_code", deviceCode);
        signMap.put("post_id", postId);
        signMap.put("score_txt", text);

        if(Integer.valueOf(sorce) <= 200){
            //小于200可以直接赠送
            paramMap.put("score", sorce);
            signMap.put("score", sorce);
            paramMap.put("sign", toSign(signMap));
            Connection.Response post = HttpUtils.postByHlx(url, paramMap);
            logger.info(post.body());
            logger.info("type：{} 、postId：{}，一次性赠送{}完毕！", type, postId, sorce);
        }else {
            for(int i=0;(Integer.valueOf(sorce)/200)>i;i++){
                paramMap.put("score", "200");
                signMap.put("score", "200");
                paramMap.put("sign", toSign(signMap));
                Connection.Response post = HttpUtils.postByHlx(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，第"+(i+1)+"次赠送200", type, postId);
            }
            if(Integer.valueOf(sorce) %200 >0){
                paramMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                signMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                paramMap.put("sign", toSign(signMap));
                Connection.Response post = HttpUtils.postByHlx(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，剩下一次性赠送: {}", type, postId, Integer.valueOf(sorce)%200);
            }
        }

    }

    /** 指定赠送使用的key
     * 赠送葫芦，不限数量（大于200会多次送）；
     * 其他引用单次的就先不改了
     * 赠送葫芦，type=2为评论，1为帖子，
     * type为2时post_id填评论id
     * @param type 2为评论，1为帖子
     * @param postId type为2时post_id填评论id
     * @param text 赠送回复文本
     * @param sorce 赠送数量
     * @param key 赠送使用的key, 为空使用系统配置key
     * @throws IOException
     */
    public void sendSorcePlus(String type, String postId, String text, String sorce, String key) throws IOException,RuntimeException {
        if(text.length()<5){
            text=text+".....";
        }
        if (StringUtils.isBlank(key)) {
            key = getKey();
        }
        String deviceCode = getDeviceCode();
        String url="https://floor.huluxia.com:443/credits/transfer/ANDROID/4.2.4?platform=2&gkey=000000&app_version=4.3.1.8&versioncode=413&market_id=tool_web&_key="+ getKey() +"&device_code="+deviceCode+"&phone_brand_type=UN";
        Map<String, String> paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        //paramMap.put("score",sorce);
        paramMap.put("score_txt",text);
        Map<String, String> signMap = new HashMap<>();
        signMap.put("_key", getKey());
        signMap.put("device_code", deviceCode);
        signMap.put("post_id", postId);
        signMap.put("score_txt", text);

        if(Integer.valueOf(sorce) <= 200){
            //小于200可以直接赠送
            paramMap.put("score", sorce);
            signMap.put("score", sorce);
            paramMap.put("sign", toSign(signMap));
            Connection.Response post = HttpUtils.postByHlx(url, paramMap);
            logger.info(post.body());
            logger.info("type：{} 、postId：{}，一次性赠送{}完毕！", type, postId, sorce);
        }else {
            for(int i=0;(Integer.valueOf(sorce)/200)>i;i++){
                paramMap.put("score", "200");
                signMap.put("score", "200");
                paramMap.put("sign", toSign(signMap));
                Connection.Response post = HttpUtils.postByHlx(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，第"+(i+1)+"次赠送200", type, postId);
            }
            if(Integer.valueOf(sorce) %200 >0){
                paramMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                signMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                paramMap.put("sign", toSign(signMap));
                Connection.Response post = HttpUtils.postByHlx(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，剩下一次性赠送: {}", type, postId, Integer.valueOf(sorce)%200);
            }
        }

    }

    /**
     * 获取一个用户的前count个帖子
     * @param count
     * @param userId
     * @return
     * @throws IOException
     */
    public static List<Posts> getPosts(String count,String userId) throws IOException {
        String postJsonUrl="http://floor.huluxia.com/post/create/list/ANDROID/2.0?start=0&count="+count+"&user_id="+userId;
        Connection.Response response = HttpUtils.get(postJsonUrl);
        String body = response.body();
        JSONObject json=new JSONObject();
        PostsJsonRootBean jsonRootBean = json.parseObject(response.body(), PostsJsonRootBean.class);
        List<Posts> posts = jsonRootBean.getPosts();
        return posts;
    }

    /**
     * 查询用户最新回复id
     * @param userId 葫芦侠用户id
     * @return
     * @throws IOException
     */
    public static String getNewCommentId(String userId) throws IOException {
        Connection.Response response = HttpUtils.get("https://floor.huluxia.com/comment/create/list/IOS/1.0?device_code=C56DC9ED259045E8885E2E016BD5B5D5&app_version=1.2.2&start=0&platform=1&count=20&user_id="+userId+"&market_id=floor_huluxia");
        String body =response.body();
        JSONObject json=new JSONObject();
        PostsJsonRootBean jsonRootBean = json.parseObject(body, PostsJsonRootBean.class);
        String commentId=String.valueOf(jsonRootBean.getComments().get(0).getCommentID());
        if(StringUtils.isBlank(commentId)){
            throw new RuntimeException("回复id查询失败");
        }
        return commentId;
    }

    /**
     * 通过userId查询UserInfo，此user类属性不全无法全部转换响应内容，不过也够用了
     * @param userId
     * @return
     * @throws IOException
     */
    public User queryUserInfo(String userId) throws IOException {
        Map<String,String> param = new HashMap<>(2);
        param.put("_key",getKey());
        param.put("user_id",userId);
        Connection.Response post = HttpUtils.post(userInfoUrl, param);
        JSONObject json=new JSONObject();
        User user = json.parseObject(post.body(), User.class);
        return user;
    }

    /**
     * 通过帖子id获取帖子详情(Jsoon)
     * @param postId
     */
    public static String getPostDetailsToJson(String postId) throws IOException {
        String postJsonUrl="http://floor.huluxia.com/post/detail/IOS/1.1?platform=2&gkey=000000&app_version=4.0.0.9.1&versioncode=20141440&market_id=floor_web&post_id="+postId+"&page_no=1&page_size=20&doc=1";
        Connection.Response response = HttpUtils.get(postJsonUrl);
        String body = response.body();
        return body;
    }

    /**
     * 签名算法
     * @param map 签名参数
     */
    public String toSign(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return getSign(""); // 处理空map边界情况
        }

        StringBuilder sb = new StringBuilder();

        // 字典序排序
        map.keySet().stream()
                .sorted() // 自然字典序
                .forEachOrdered(key -> appendEntry(sb, key, map.get(key)));
        logger.info("sb: {}", sb.toString());
        return getSign(sb.toString());
    }

    private void appendEntry(StringBuilder sb, String key, String value) {
        sb.append(key)
                .append(value != null ? value : ""); // 显式处理null值
    }

    /**
     * 随机设备码
     * @return  设备码
     */
    public String getDeviceCode() {
        String randomCode = UUID.randomUUID().toString();
        return "[d]" + randomCode;
    }

    /**
     * 签名
     * @param text  签名参数字符串
     * @return  sign
     */
    public static String getSign(String text) {
        // 拼接原始文本和固定盐值
        String combined = text + "dc9ae0b1c8bae7ccf421cd1607bc3b14";

        try {
            // 创建 MD5 摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新字节数据并生成哈希
            byte[] hashBytes = md.digest(combined.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // 理论上 MD5 算法在所有 Java 平台都可用
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    // 将字节数组转换为十六进制表示的辅助方法
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b); // 确保无符号转换
            if (hex.length() == 1) {
                hexString.append('0'); // 补零保证两位长度
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
