package top.jsls9.oajsfx.enums;

import java.util.HashMap;
import java.util.Map;

/** 帖子前置逻辑变量枚举
 * @author bSu
 * @date 2022/1/8 - 18:19
 */
public enum PostLogicVariable {

    TITLE("title","post.title"),//标题
    DETAIL("detail","post.detail"),//帖子内容
    HIT("hit","post.hit"),//帖子热度（浏览量）
    COMMENTCOUNT("commentCount","post.commentCount"),//回复数量
    score("score","post.score"),//帖子被挂红数量
    CREATETIME("createTime","post.createTime"),//帖子创建时间
    CATEGORYID("categoryID","post.categoryID"),//帖子所在板块ID
    TAGID("tagID","post.tagID"),//帖子所在分区
    PRAISE("praise","post.praise"),//帖子被点赞的数量
    ISGOOD("isGood","post.isGood"),//帖子是否为精贴，1=是、0=否


    IDENTITYTITLE("identityTitle","post.user.identityTitle"),//楼主称号
    LEVEL("level","post.user.level"),//楼主等级
    NICK("nick","post.user.nick")//楼主昵称

    ;

    //变量名
    private String name;
    //json取值标记
    private String value;

    //key加$用作匹配变量
    public static Map<String, String> typeMap = new HashMap<String, String>();

    static {
        for (PostLogicVariable p : PostLogicVariable.values()) {
            typeMap.put("$"+p.getName(),p.getValue());
        }
    }

    PostLogicVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String name){
        for (PostLogicVariable p : PostLogicVariable.values()) {
            if( name.equals(p.getName()) ){
                return p.getValue();
            }
        }
        return null;
    }



}
