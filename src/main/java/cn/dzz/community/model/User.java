package cn.dzz.community.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String accountId;
    private String token;
    private String login;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
