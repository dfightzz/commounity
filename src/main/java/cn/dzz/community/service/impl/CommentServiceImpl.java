package cn.dzz.community.service.impl;

import cn.dzz.community.CommentType;
import cn.dzz.community.entity.Comment;
import cn.dzz.community.dao.CommentDao;
import cn.dzz.community.entity.Notification;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import cn.dzz.community.service.CommentService;
import cn.dzz.community.service.NotificationService;
import cn.dzz.community.service.QuestionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2020-03-23 15:24:34
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    @Autowired
    private UserInterface userInterface;

    @Autowired
    private QuestionService2 questionService2;


    @Autowired
    private NotificationService notificationService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Comment queryById(Long id) {
        return this.commentDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Comment> queryAllByLimit(int offset, int limit) {
        return this.commentDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Comment insert(Comment comment) {
        // 查找改评论的上一个层次
        if (CommentType.COMMENT_TYPE.getCode() == comment.getType()) {
            Comment parentComment = commentDao.queryById(comment.getParentId());
            if (parentComment == null) {
                throw new RuntimeException("父级评论不存在");
            }
            commentDao.incReply(parentComment);

            Question question = questionService2.queryById((Integer.valueOf(String.valueOf(parentComment.getParentId()))));
            insertNotification(comment, 1, parentComment.getCommentator(), parentComment.getContent(), parentComment.getId());
            if (!question.getCreator().equals(parentComment.getCommentator())) {
                insertNotification(comment, 0, question.getCreator(), question.getTitle(), question.getId());
            }

        } else if (CommentType.QUESTION_TPYE.getCode() == comment.getType()) {

            Question question = questionService2.queryById((Integer.valueOf(String.valueOf(comment.getParentId()))));
            if (question == null) {
                throw new RuntimeException("父级问题不存在");
            }
            questionService2.incCommnet(question);
            insertNotification(comment, 0, question.getCreator(), question.getTitle(), question.getId());
        } else {
            throw new RuntimeException("评论类型不存在");
        }
        this.commentDao.insert(comment);
        return comment;
    }

    private void insertNotification(Comment comment, int type, int parentUserId, String parentContent, long parentId) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        User notifier = userInterface.findUserById(comment.getCommentator());
        notification.setNotifierName(notifier.getLogin());
        notification.setStatus(0);

        notification.setReceiver(parentUserId);
        User receiver = userInterface.findUserById(parentUserId);
        notification.setReceiverName(receiver.getLogin());
        notification.setTitle(parentContent);
        notification.setOuterId(parentId);
        notificationService.insert(notification);
    }

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment update(Comment comment) {
        this.commentDao.update(comment);
        return this.queryById(comment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.commentDao.deleteById(id) > 0;
    }

    @Override
    public List<Comment> getByCommentId(Long id, int i) {
        Comment commnetExample = new Comment();
        commnetExample.setParentId(id);
        commnetExample.setType(i);
        List<Comment> comments = commentDao.queryAll(commnetExample);
        Set<Integer> collect = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        Map<Integer, User> userMap = collect.stream()
                .map(userId -> userInterface.findUserById(userId))
                .collect(Collectors.toMap(user -> user.getId(), user -> user));
        comments.forEach(comment -> comment.setUser(userMap.get(comment.getCommentator())));

        return comments;
    }
}