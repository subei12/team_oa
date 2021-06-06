/**
  * Copyright 2020 bejson.com 
  */
package top.jsls9.oajsfx.hlxPojo;

import java.util.List;

/**
 * Auto-generated: 2020-11-30 21:27:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Posts {

    private long postID;
    private String title;
    private String detail;
    private List<String> images;
    private int score;
    private String scoreTxt;
    private int hit;
    private int commentCount;
    private int notice;
    private int weight;
    private int isGood;
    private long createTime;
    private long activeTime;
    private int line;
    private User user;
    private String ext;
    private Category category;
    private int tagid;
    private int status;
    private int praise;
    private String voice;
    private int isAuthention;
    private int isRich;
    private int appOrientation;
    private int isAppPost;
    private String appVersion;
    private int appSize;
    private String appSystem;
    private String appLogo;
    private String screenshots;
    private String appIntroduce;
    private String appUrl;
    private String appLanguage;
    private int isGif;
    public void setPostID(long postID) {
         this.postID = postID;
     }
     public long getPostID() {
         return postID;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setDetail(String detail) {
         this.detail = detail;
     }
     public String getDetail() {
         return detail;
     }

    public void setImages(List<String> images) {
         this.images = images;
     }
     public List<String> getImages() {
         return images;
     }

    public void setScore(int score) {
         this.score = score;
     }
     public int getScore() {
         return score;
     }

    public void setScoreTxt(String scoreTxt) {
         this.scoreTxt = scoreTxt;
     }
     public String getScoreTxt() {
         return scoreTxt;
     }

    public void setHit(int hit) {
         this.hit = hit;
     }
     public int getHit() {
         return hit;
     }

    public void setCommentCount(int commentCount) {
         this.commentCount = commentCount;
     }
     public int getCommentCount() {
         return commentCount;
     }

    public void setNotice(int notice) {
         this.notice = notice;
     }
     public int getNotice() {
         return notice;
     }

    public void setWeight(int weight) {
         this.weight = weight;
     }
     public int getWeight() {
         return weight;
     }

    public void setIsGood(int isGood) {
         this.isGood = isGood;
     }
     public int getIsGood() {
         return isGood;
     }

    public void setCreateTime(long createTime) {
         this.createTime = createTime;
     }
     public long getCreateTime() {
         return createTime;
     }

    public void setActiveTime(long activeTime) {
         this.activeTime = activeTime;
     }
     public long getActiveTime() {
         return activeTime;
     }

    public void setLine(int line) {
         this.line = line;
     }
     public int getLine() {
         return line;
     }

    public void setUser(User user) {
         this.user = user;
     }
     public User getUser() {
         return user;
     }

    public void setExt(String ext) {
         this.ext = ext;
     }
     public String getExt() {
         return ext;
     }

    public void setCategory(Category category) {
         this.category = category;
     }
     public Category getCategory() {
         return category;
     }

    public void setTagid(int tagid) {
         this.tagid = tagid;
     }
     public int getTagid() {
         return tagid;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setPraise(int praise) {
         this.praise = praise;
     }
     public int getPraise() {
         return praise;
     }

    public void setVoice(String voice) {
         this.voice = voice;
     }
     public String getVoice() {
         return voice;
     }

    public void setIsAuthention(int isAuthention) {
         this.isAuthention = isAuthention;
     }
     public int getIsAuthention() {
         return isAuthention;
     }

    public void setIsRich(int isRich) {
         this.isRich = isRich;
     }
     public int getIsRich() {
         return isRich;
     }

    public void setAppOrientation(int appOrientation) {
         this.appOrientation = appOrientation;
     }
     public int getAppOrientation() {
         return appOrientation;
     }

    public void setIsAppPost(int isAppPost) {
         this.isAppPost = isAppPost;
     }
     public int getIsAppPost() {
         return isAppPost;
     }

    public void setAppVersion(String appVersion) {
         this.appVersion = appVersion;
     }
     public String getAppVersion() {
         return appVersion;
     }

    public void setAppSize(int appSize) {
         this.appSize = appSize;
     }
     public int getAppSize() {
         return appSize;
     }

    public void setAppSystem(String appSystem) {
         this.appSystem = appSystem;
     }
     public String getAppSystem() {
         return appSystem;
     }

    public void setAppLogo(String appLogo) {
         this.appLogo = appLogo;
     }
     public String getAppLogo() {
         return appLogo;
     }

    public void setScreenshots(String screenshots) {
         this.screenshots = screenshots;
     }
     public String getScreenshots() {
         return screenshots;
     }

    public void setAppIntroduce(String appIntroduce) {
         this.appIntroduce = appIntroduce;
     }
     public String getAppIntroduce() {
         return appIntroduce;
     }

    public void setAppUrl(String appUrl) {
         this.appUrl = appUrl;
     }
     public String getAppUrl() {
         return appUrl;
     }

    public void setAppLanguage(String appLanguage) {
         this.appLanguage = appLanguage;
     }
     public String getAppLanguage() {
         return appLanguage;
     }

    public void setIsGif(int isGif) {
         this.isGif = isGif;
     }
     public int getIsGif() {
         return isGif;
     }


    @Override
    public String toString() {
        return "Posts{" +
                "postID=" + postID +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", images=" + images +
                ", score=" + score +
                ", scoreTxt='" + scoreTxt + '\'' +
                ", hit=" + hit +
                ", commentCount=" + commentCount +
                ", notice=" + notice +
                ", weight=" + weight +
                ", isGood=" + isGood +
                ", createTime=" + createTime +
                ", activeTime=" + activeTime +
                ", line=" + line +
                ", user=" + user +
                ", ext='" + ext + '\'' +
                ", category=" + category +
                ", tagid=" + tagid +
                ", status=" + status +
                ", praise=" + praise +
                ", voice='" + voice + '\'' +
                ", isAuthention=" + isAuthention +
                ", isRich=" + isRich +
                ", appOrientation=" + appOrientation +
                ", isAppPost=" + isAppPost +
                ", appVersion='" + appVersion + '\'' +
                ", appSize=" + appSize +
                ", appSystem='" + appSystem + '\'' +
                ", appLogo='" + appLogo + '\'' +
                ", screenshots='" + screenshots + '\'' +
                ", appIntroduce='" + appIntroduce + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", appLanguage='" + appLanguage + '\'' +
                ", isGif=" + isGif +
                '}';
    }
}