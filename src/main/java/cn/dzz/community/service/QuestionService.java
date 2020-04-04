package cn.dzz.community.service;

import cn.dzz.community.dao.CommentDao;
import cn.dzz.community.dao.QuestionDao;


import cn.dzz.community.entity.Comment;
import cn.dzz.community.mapper.QuestionMapper;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class QuestionService {


    @Autowired
    private UserInterface userInterface;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CommentDao commentDao;

    public PageInfo<Question> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.list(userId);
        PageInfo<Question> pageInfo = new PageInfo<Question>(list);
        for (Question question : list) {
            User user = userInterface.findUserById(question.getCreator());
            question.setUser(user);
        }
        if (pageInfo.getPages() > 5) {
            if (pageNum < pageSize / 2 + 1) {
                pageInfo.setStartRow(1);
                pageInfo.setEndRow(pageSize);
            } else if (pageNum > pageInfo.getPages() - (pageSize / 2 + 1)) {
                pageInfo.setEndRow(pageInfo.getPages());
                pageInfo.setStartRow(pageInfo.getPages() - pageSize + 1);
            } else {
                pageInfo.setStartRow(pageNum - 2);
                pageInfo.setEndRow(pageNum + 2);
            }
        }else {
            pageInfo.setStartRow(1);
            pageInfo.setEndRow(pageInfo.getPages());
        }
        return pageInfo;
    }

    public Question getQuestionById(Integer id, int type) {
        Question question = questionMapper.getQuestionById(id);

        Comment commnetExample = new Comment();
        commnetExample.setParentId(Long.valueOf(question.getId()));
        commnetExample.setType(type);
        List<Comment> comments = commentDao.queryAll(commnetExample);

        Set<Integer> collect = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        Map<Integer, User> userMap = collect.stream()
                .map(userId -> userInterface.findUserById(userId))
                .collect(Collectors.toMap(user -> user.getId(), user -> user));
        comments.forEach(comment -> comment.setUser(userMap.get(comment.getCommentator())));
        question.setComments(comments);

        User user = userInterface.findUserById(question.getCreator());
        question.setUser(user);
        return question;
    }

    @Transactional
    public void incView(Integer id) {
        Question questionById = questionMapper.getQuestionById(id);
        questionById.setViewCount(questionById.getViewCount() + 1);
        questionDao.update(questionById);
    }

    public List<Question> selectRelive(Question question) {
        String replaceTag = question.getTag().replace(",", "|");
        question.setTag(replaceTag);
        return questionDao.selectRelive(question);
    }


}
