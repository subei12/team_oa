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
public class PostsJsonRootBean {

    private String msg;
    private Post post;
    private List<Posts> posts;
    private List<Comments> comments;
    private List<String> praiselist;
    private int categoryID;
    private int scoreUserCount;
    private int currPageNo;
    private int pageSize;
    private int totalPage;
    private List<RemindUsers> remindUsers;
    private String studioInfo;
    private int isTmp;
    private int status;
    private Category category;
    private int more;
    private String start;
    //板块置顶贴，于posts对象一样，只是变量名不一样
    private List<Posts> weightandtoppost;

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Category getCategory() {
        return category;
    }

    public int getMore() {
        return more;
    }

    public String getStart() {
        return start;
    }

    public void setWeightandtoppost(List<Posts> weightandtoppost) {
        this.weightandtoppost = weightandtoppost;
    }

    public List<Posts> getWeightandtoppost() {
        return weightandtoppost;
    }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setPost(Post post) {
         this.post = post;
     }
     public Post getPost() {
         return post;
     }

    public void setComments(List<Comments> comments) {
         this.comments = comments;
     }
     public List<Comments> getComments() {
         return comments;
     }

    public void setPraiselist(List<String> praiselist) {
         this.praiselist = praiselist;
     }
     public List<String> getPraiselist() {
         return praiselist;
     }

    public void setCategoryID(int categoryID) {
         this.categoryID = categoryID;
     }
     public int getCategoryID() {
         return categoryID;
     }

    public void setScoreUserCount(int scoreUserCount) {
         this.scoreUserCount = scoreUserCount;
     }
     public int getScoreUserCount() {
         return scoreUserCount;
     }

    public void setCurrPageNo(int currPageNo) {
         this.currPageNo = currPageNo;
     }
     public int getCurrPageNo() {
         return currPageNo;
     }

    public void setPageSize(int pageSize) {
         this.pageSize = pageSize;
     }
     public int getPageSize() {
         return pageSize;
     }

    public void setTotalPage(int totalPage) {
         this.totalPage = totalPage;
     }
     public int getTotalPage() {
         return totalPage;
     }

    public void setRemindUsers(List<RemindUsers> remindUsers) {
         this.remindUsers = remindUsers;
     }
     public List<RemindUsers> getRemindUsers() {
         return remindUsers;
     }

    public void setStudioInfo(String studioInfo) {
         this.studioInfo = studioInfo;
     }
     public String getStudioInfo() {
         return studioInfo;
     }

    public void setIsTmp(int isTmp) {
         this.isTmp = isTmp;
     }
     public int getIsTmp() {
         return isTmp;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public List<Posts> getPosts() {
        return posts;
    }
}