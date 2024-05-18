package top.jsls9.oajsfx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.hlxPojo.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${hlx.key}")
    private String key;

    @Value("${hlx.url.userInfo}")
    private String userInfoUrl;

    /**
     * 获取key
     * @return
     */
    public String getKey(){
        return this.key;
        // try {
        //     String url=getKeyUrl;
        //     Connection.Response response = HttpUtils.get(url);
        //     String body = response.body();
        //     JSONObject json=new JSONObject();
        //     GetKeyRootBean getKeyRootBean = json.parseObject(response.body(), GetKeyRootBean.class);
        //     return getKeyRootBean.get_key();
        // }catch (Exception e){
        //     logger.error("获取key失败，",e);
        //     return null;
        // }
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
        String url="http://floor.huluxia.com/credits/transfer/ANDROID/2.0?platform=2&gkey=000000&app_version=4.0.0.1&versioncode=20141415&market_id=floor_web&_key="+getKey()+"&device_code=%5Bw%5D02%3A00%3A00%3A00%3A00%3A00%5Bd%5D830b9382-7eef-4557-b585-afd28f674fe5";
        Map<String,String > paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        paramMap.put("score",sorce);
        paramMap.put("score_txt",text);
        Connection.Response post = HttpUtils.post(url, paramMap);
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
        String url="http://floor.huluxia.com/credits/transfer/ANDROID/2.0?platform=2&gkey=000000&app_version=4.0.0.1&versioncode=20141415&market_id=floor_web&_key="+getKey()+"&device_code=%5Bw%5D02%3A00%3A00%3A00%3A00%3A00%5Bd%5D830b9382-7eef-4557-b585-afd28f674fe5";
        Map<String, String> paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        //paramMap.put("score",sorce);
        paramMap.put("score_txt",text);
        if(Integer.valueOf(sorce) <= 200){
            //小于200可以直接赠送
            paramMap.put("score", sorce);
            Connection.Response post = HttpUtils.post(url, paramMap);
            logger.info(post.body());
            logger.info("type：{} 、postId：{}，一次性赠送{}完毕！", type, postId, sorce);
        }else {
            for(int i=0;(Integer.valueOf(sorce)/200)>i;i++){
                paramMap.put("score", "200");
                Connection.Response post = HttpUtils.post(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，第"+(i+1)+"次赠送200", type, postId);
            }
            if(Integer.valueOf(sorce) %200 >0){
                paramMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                Connection.Response post = HttpUtils.post(url, paramMap);
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
        String url="http://floor.huluxia.com/credits/transfer/ANDROID/2.0?platform=2&gkey=000000&app_version=4.0.0.1&versioncode=20141415&market_id=floor_web&_key="+key+"&device_code=%5Bw%5D02%3A00%3A00%3A00%3A00%3A00%5Bd%5D830b9382-7eef-4557-b585-afd28f674fe5";
        Map<String, String> paramMap=new HashMap();
        paramMap.put("post_id",postId);
        paramMap.put("type_id",type);
        paramMap.put("isadmin","0");
        //paramMap.put("score",sorce);
        paramMap.put("score_txt",text);
        if(Integer.valueOf(sorce) <= 200){
            //小于200可以直接赠送
            paramMap.put("score", sorce);
            Connection.Response post = HttpUtils.post(url, paramMap);
            logger.info(post.body());
            logger.info("type：{} 、postId：{}，一次性赠送{}完毕！", type, postId, sorce);
        }else {
            for(int i=0;(Integer.valueOf(sorce)/200)>i;i++){
                paramMap.put("score", "200");
                Connection.Response post = HttpUtils.post(url, paramMap);
                logger.info(post.body());
                logger.info("type：{} 、postId：{}，第"+(i+1)+"次赠送200", type, postId);
            }
            if(Integer.valueOf(sorce) %200 >0){
                paramMap.put("score", String.valueOf( Integer.valueOf(sorce) %200 ));
                Connection.Response post = HttpUtils.post(url, paramMap);
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


}
