package cn.dzz.community.service;

import cn.dzz.community.dao.TbUserDao;
import cn.dzz.community.dto.QuestionVo;
import cn.dzz.community.entity.TbUser;
import cn.dzz.community.mapper.QuestionMapper;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionService {


    @Autowired
    private UserInterface userInterface;

    @Autowired
    private QuestionMapper questionMapper;

    public PageInfo<Question> list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.list(userId);
        PageInfo<Question> pageInfo = new PageInfo<Question>(list);
        for (Question question : list) {
            User user = userInterface.findUserById(question.getCreator());
            question.setUser(user);
        }
        if (pageNum < pageSize / 2 + 1) {
            pageInfo.setStartRow(1);
            pageInfo.setEndRow(pageSize);
        } else if (pageNum > pageInfo.getPages() - (pageSize / 2 + 1)){
            pageInfo.setEndRow(pageInfo.getPages());
            pageInfo.setStartRow(pageInfo.getPages() - pageSize + 1);
        } else {
            pageInfo.setStartRow(pageNum - 2);
            pageInfo.setEndRow(pageNum + 2);
        }

        return pageInfo;
    }

    public Question getQuestionById(Integer id) {

        Question question = questionMapper.getQuestionById(id);
        User user = userInterface.findUserById(question.getCreator());

        question.setUser(user);
        return question;
    }
}
