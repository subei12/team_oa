package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * hlx_category_data
 * @author 
 */
public class HlxCategoryData implements Serializable {
    private Integer id;

    /**
     * 录入数据的日期
     */
    private Date date;

    /**
     * 板块id
     */
    private Integer catId;

    /**
     * 板块名称
     */
    private String catTitle;

    /**
     * 板块数据
     */
    private String catData;

    /**
     * 类型；0-板块热度数据，1-当日最新帖子（取当前时间泳池的最新帖子）
     */
    private Integer type;

    private static final long serialVersionUID = 1L;

    /**
     * 内部类，用于生成HlxCategoryData.catData的json数据
     */
    public static class CatDate{

        /**
         * type = 0 板块热度
         */
        private long viewCount;

        /**
         * type = 0 帖子总数
         */
        private long postCount;

        /**
         * type = 1 当日最新帖子id
         */
        private long postId;

        /**
         * type = 0 较昨日新增热度
         */
        private long addViewCount;

        /**
         * type = 0 较昨日新增帖子数量
         */
        private long addPostCount;

        public CatDate() {
        }

        public CatDate(long viewCount, long postCount, long postId, long addViewCount, long addPostCount) {
            this.viewCount = viewCount;
            this.postCount = postCount;
            this.postId = postId;
            this.addViewCount = addViewCount;
            this.addPostCount = addPostCount;
        }

        public long getViewCount() {
            return viewCount;
        }

        public long getPostCount() {
            return postCount;
        }

        public long getPostId() {
            return postId;
        }

        public void setViewCount(long viewCount) {
            this.viewCount = viewCount;
        }

        public void setPostCount(long postCount) {
            this.postCount = postCount;
        }

        public void setPostId(long postId) {
            this.postId = postId;
        }


        public long getAddViewCount() {
            return addViewCount;
        }

        public long getAddPostCount() {
            return addPostCount;
        }

        public void setAddViewCount(long addViewCount) {
            this.addViewCount = addViewCount;
        }

        public void setAddPostCount(long addPostCount) {
            this.addPostCount = addPostCount;
        }

        @Override
        public String toString() {
            return "CatDate{" +
                    "viewCount='" + viewCount + '\'' +
                    ", postCount='" + postCount + '\'' +
                    ", postId='" + postId + '\'' +
                    ", addViewCount='" + addViewCount + '\'' +
                    ", addPostCount='" + addPostCount + '\'' +
                    '}';
        }

    }


    public HlxCategoryData() {
    }

    public HlxCategoryData(Integer id, Date date, Integer catId, String catTitle, String catData, Integer type) {
        this.id = id;
        this.date = date;
        this.catId = catId;
        this.catTitle = catTitle;
        this.catData = catData;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Integer getCatId() {
        return catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public String getCatData() {
        return catData;
    }

    public Integer getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public void setCatData(String catData) {
        this.catData = catData;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HlxCategoryData{" +
                "id=" + id +
                ", date=" + date +
                ", catId=" + catId +
                ", catTitle='" + catTitle + '\'' +
                ", catData='" + catData + '\'' +
                ", type=" + type +
                '}';
    }
}