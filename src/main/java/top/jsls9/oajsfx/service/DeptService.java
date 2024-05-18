package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.model.Dept;

import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 23:58
 */
public interface DeptService {

    /**
     * 分页查询
     * @param page
     * @param perPage
     * @return
     */
    Map<String,Object> getDeptsByPage(Integer page, Integer perPage);

    /**
     * 不分页查询
     * @return
     */
    List<Dept> getDepts();

    int insertWebSort(Dept websort);

    int updateNameById(Dept webSort);

    int delDeptById(String[] ids);

    /**
     * 通过 deptId 修改团队信息
     * @param dept 团队id
     * @return int
     */
    int updateDeptById(Dept dept);

}
