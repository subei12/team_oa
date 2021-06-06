package top.jsls9.oajsfx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.jsls9.oajsfx.model.WebDetails;

import java.util.List;

@Mapper
@Repository
public interface WebDetailsDao {
    int deleteByPrimaryKey(String id);

    int insert(WebDetails record);

    int insertSelective(WebDetails record);

    WebDetails selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WebDetails record);

    int updateByPrimaryKey(WebDetails record);

    List<WebDetails> getWebDetails();

    List<WebDetails> getWebDetailsByWebPath(String webPath);

    void updateById(WebDetails webDetails);
}