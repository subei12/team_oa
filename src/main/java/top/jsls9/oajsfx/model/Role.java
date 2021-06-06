package top.jsls9.oajsfx.model;

import java.io.Serializable;

/**
 * role
 * @author 
 */
public class Role implements Serializable {
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

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
}