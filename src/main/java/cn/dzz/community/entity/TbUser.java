package cn.dzz.community.entity;

import java.io.Serializable;

/**
 * (TbUser)实体类
 *
 * @author makejava
 * @since 2020-03-22 11:10:15
 */
public class TbUser implements Serializable {
    private static final long serialVersionUID = -13706713473972011L;
    
    private Integer id;
    
    private String accountId;
    
    private String name;
    
    private String token;
    
    private String login;
    
    private Long gmtCreated;
    
    private Long gmtModified;
    
    private String avatarUrl;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Long gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}