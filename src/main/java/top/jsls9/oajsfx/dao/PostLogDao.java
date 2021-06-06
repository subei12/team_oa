package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.PostLog;

@Mapper
@Repository
public interface PostLogDao {
    int deleteByPrimaryKey(String postId);

    int insert(PostLog record);

    int insertSelective(PostLog record);

    PostLog selectByPrimaryKey(String postId);

    int updateByPrimaryKeySelective(PostLog record);

    int updateByPrimaryKey(PostLog record);

    PostLog getPostLogByHlxPostId(String hlxPostId);
}