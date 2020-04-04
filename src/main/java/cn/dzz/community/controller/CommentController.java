package cn.dzz.community.controller;

import cn.dzz.community.dto.CommentDto;
import cn.dzz.community.entity.Comment;
import cn.dzz.community.model.User;
import cn.dzz.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2020-03-23 15:24:34
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Comment selectOne(Long id) {
        return this.commentService.queryById(id);
    }

    @ResponseBody
    @PostMapping("/post")
    public Object post(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        Map<Object, Object> map = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            map.put("errorCode", 2001);
            map.put("errorMsg", "该操作需要您进行登录，请登录！");
            return map;
        }
        Comment comment = new Comment();

        try {
            BeanUtils.copyProperties(commentDto, comment);
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            comment.setCommentator(user.getId());
            comment.setReplyCount(0);
            commentService.insert(comment);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("errorCode", 500);
            map.put("errorMsg", e.getMessage());
            return map;
        }
        map.put("errorCode", 200);
        map.put("errorMsg", "回复成功");
        return map;
    }

    @ResponseBody
    @GetMapping("/getByCommentId/{id}")
    public Object getByCommentId(@PathVariable(name = "id") Long id) {
        List<Comment> comments = commentService.getByCommentId(id, 1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errorCode", 200);
        map.put("errorMsg", "");
        map.put("commentList", comments);
        return map;
    }

}