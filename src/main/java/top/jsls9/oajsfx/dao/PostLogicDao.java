package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.PostLogic;

import java.util.List;

@Mapper
@Repository
public interface PostLogicDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLogic record);

    int insertSelective(PostLogic record);

    PostLogic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLogic record);

    int updateByPrimaryKey(PostLogic record);

    List<PostLogic> queryPostLogicList();

    int delete(String[] ids);
}