package cn.dzz.community.service.impl;


import cn.dzz.community.dao.QuestionDao;

import cn.dzz.community.model.Question;
import cn.dzz.community.service.QuestionService2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Question)表服务实现类
 *
 * @author makejava
 * @since 2020-03-22 11:10:13
 */
@Service("questionService2")
public class QuestionServiceImpl implements QuestionService2 {
    @Resource
    private QuestionDao questionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Question queryById(Integer id) {
        return this.questionDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Question> queryAllByLimit(int offset, int limit) {
        return this.questionDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param question 实例对象
     * @return 实例对象
     */
    @Override
    public Question insert(Question question) {
        this.questionDao.insert(question);
        return question;
    }

    /**
     * 修改数据
     *
     * @param question 实例对象
     * @return 实例对象
     */
    @Override
    public Question update(Question question) {
        this.questionDao.update(question);
        return this.queryById(question.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.questionDao.deleteById(id) > 0;
    }

    @Override
    public void incCommnet(Question question) {
        this.questionDao.incCommnet(question);
    }
}