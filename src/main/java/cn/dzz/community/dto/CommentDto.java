package cn.dzz.community.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Integer type;
    private String content;
    private Long parentId;
}
