package top.jsls9.oajsfx.model;

import java.io.Serializable;

/**
 * role_permission
 * @author 
 */
public class RolePermission implements Serializable {
    private Integer id;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}