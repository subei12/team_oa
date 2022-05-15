package top.jsls9.oajsfx.vo;

/**懒得建个dto的包了
 * @author bSu
 * @date 2022/5/8 - 15:28
 */
public class RoleDTO {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态，0-可用、1-删除
     */
    private Integer status;

    /**
     * 角色等级，2级可分配3级角色
     */
    private Integer level;

    /**
     * 权限id，多个以“,”分割
     */
    private String permissionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
