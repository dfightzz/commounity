package cn.dzz.community.model;

import lombok.Data;

@Data
public class Question {

    private Integer id;
    private String title;
    private String description;
    private long gmtCreate;
    private long gmtModified;
    private int creator;
    private int commitCount;
    private int viewCount;
    private int likeCount;
    private String tag;
    private User user;
}
