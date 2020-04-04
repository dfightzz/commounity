package cn.dzz.community.model;

import cn.dzz.community.entity.Comment;
import lombok.Data;

import java.util.List;

@Data
public class Question {

    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commitCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
    List<Comment> comments;
}
