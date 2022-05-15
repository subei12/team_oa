package top.jsls9.oajsfx.vo;

import top.jsls9.oajsfx.model.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bSu
 * @date 2022/5/1 - 20:01
 */
public class PermissionInputTreeVo {

    private String label;

    private String value;

    private Integer parentId;


    List<PermissionInputTreeVo> children = new ArrayList<>();

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<PermissionInputTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionInputTreeVo> children) {
        this.children = children;
    }

}
