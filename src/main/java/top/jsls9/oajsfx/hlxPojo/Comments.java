/**
  * Copyright 2020 bejson.com 
  */
package top.jsls9.oajsfx.hlxPojo;
import java.util.List;

/**
 * Auto-generated: 2020-11-28 15:44:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Comments {

    private long commentID;
    private String text;
    private List<String> images;
    private String voice;
    private int voiceTime;
    private int score;
    private String scoreTxt;
    private int seq;
    private long createTime;
    private int state;
    private User user;
    private String post;
    private String category;
    private String refComment;
    private List<String> scorelist;
    private int scoreUserCount;
    private int scorecount;
    private int praise;
    private String remindUsers;
    public void setCommentID(long commentID) {
         this.commentID = commentID;
     }
     public long getCommentID() {
         return commentID;
     }

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setImages(List<String> images) {
         this.images = images;
     }
     public List<String> getImages() {
         return images;
     }

    public void setVoice(String voice) {
         this.voice = voice;
     }
     public String getVoice() {
         return voice;
     }

    public void setVoiceTime(int voiceTime) {
         this.voiceTime = voiceTime;
     }
     public int getVoiceTime() {
         return voiceTime;
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

    public void setSeq(int seq) {
         this.seq = seq;
     }
     public int getSeq() {
         return seq;
     }

    public void setCreateTime(long createTime) {
         this.createTime = createTime;
     }
     public long getCreateTime() {
         return createTime;
     }

    public void setState(int state) {
         this.state = state;
     }
     public int getState() {
         return state;
     }

    public void setUser(User user) {
         this.user = user;
     }
     public User getUser() {
         return user;
     }

    public void setPost(String post) {
         this.post = post;
     }
     public String getPost() {
         return post;
     }

    public void setCategory(String category) {
         this.category = category;
     }
     public String getCategory() {
         return category;
     }

    public void setRefComment(String refComment) {
         this.refComment = refComment;
     }
     public String getRefComment() {
         return refComment;
     }

    public void setScorelist(List<String> scorelist) {
         this.scorelist = scorelist;
     }
     public List<String> getScorelist() {
         return scorelist;
     }

    public void setScoreUserCount(int scoreUserCount) {
         this.scoreUserCount = scoreUserCount;
     }
     public int getScoreUserCount() {
         return scoreUserCount;
     }

    public void setScorecount(int scorecount) {
         this.scorecount = scorecount;
     }
     public int getScorecount() {
         return scorecount;
     }

    public void setPraise(int praise) {
         this.praise = praise;
     }
     public int getPraise() {
         return praise;
     }

    public void setRemindUsers(String remindUsers) {
         this.remindUsers = remindUsers;
     }
     public String getRemindUsers() {
         return remindUsers;
     }

}