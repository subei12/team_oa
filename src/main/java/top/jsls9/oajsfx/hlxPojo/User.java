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
public class User {

    private long userID;
    private String nick;
    private String avatar;
    private int gender;
    private int age;
    private int role;
    private long experience;
    private int credits;
    private String identityTitle;
    private long identityColor;
    private int level;
    private long levelColor;
    private int integral;
    private int uuid;
    private String integralNick;
    private String signature;
    private List<MedalList> medalList;

    //获取key的接口补充
    private long birthday;
    private int needSetPassword;
    private int needSetUserInfo;

    public void setUserID(long userID) {
         this.userID = userID;
     }
     public long getUserID() {
         return userID;
     }

    public void setNick(String nick) {
         this.nick = nick;
     }
     public String getNick() {
         return nick;
     }

    public void setAvatar(String avatar) {
         this.avatar = avatar;
     }
     public String getAvatar() {
         return avatar;
     }

    public void setGender(int gender) {
         this.gender = gender;
     }
     public int getGender() {
         return gender;
     }

    public void setAge(int age) {
         this.age = age;
     }
     public int getAge() {
         return age;
     }

    public void setRole(int role) {
         this.role = role;
     }
     public int getRole() {
         return role;
     }

    public void setExperience(long experience) {
         this.experience = experience;
     }
     public long getExperience() {
         return experience;
     }

    public void setCredits(int credits) {
         this.credits = credits;
     }
     public int getCredits() {
         return credits;
     }

    public void setIdentityTitle(String identityTitle) {
         this.identityTitle = identityTitle;
     }
     public String getIdentityTitle() {
         return identityTitle;
     }

    public void setIdentityColor(long identityColor) {
         this.identityColor = identityColor;
     }
     public long getIdentityColor() {
         return identityColor;
     }

    public void setLevel(int level) {
         this.level = level;
     }
     public int getLevel() {
         return level;
     }

    public void setLevelColor(long levelColor) {
         this.levelColor = levelColor;
     }
     public long getLevelColor() {
         return levelColor;
     }

    public void setIntegral(int integral) {
         this.integral = integral;
     }
     public int getIntegral() {
         return integral;
     }

    public void setUuid(int uuid) {
         this.uuid = uuid;
     }
     public int getUuid() {
         return uuid;
     }

    public void setIntegralNick(String integralNick) {
         this.integralNick = integralNick;
     }
     public String getIntegralNick() {
         return integralNick;
     }

    public void setSignature(String signature) {
         this.signature = signature;
     }
     public String getSignature() {
         return signature;
     }

    public void setMedalList(List<MedalList> medalList) {
         this.medalList = medalList;
     }
     public List<MedalList> getMedalList() {
         return medalList;
     }

    public long getBirthday() {
        return birthday;
    }

    public int getNeedSetPassword() {
        return needSetPassword;
    }

    public int getNeedSetUserInfo() {
        return needSetUserInfo;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setNeedSetPassword(int needSetPassword) {
        this.needSetPassword = needSetPassword;
    }

    public void setNeedSetUserInfo(int needSetUserInfo) {
        this.needSetUserInfo = needSetUserInfo;
    }
}