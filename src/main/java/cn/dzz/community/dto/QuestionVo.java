package cn.dzz.community.dto;

import cn.dzz.community.model.User;
import lombok.Data;

@Data
public class QuestionVo {
    private int id;
    private String title;
    private String description;
    private long gmtCreate;
    private long gmtModified;
    private int creator;
    private int commitCount;
    private int viewCount;
    private int likeCount;
    private String tag;

}
