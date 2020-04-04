package cn.dzz.community.service.impl;

import cn.dzz.community.entity.Notification;
import cn.dzz.community.dao.NotificationDao;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import cn.dzz.community.service.NotificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Notification)表服务实现类
 *
 * @author makejava
 * @since 2020-04-04 13:05:41
 */
@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
    @Resource
    private NotificationDao notificationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Notification queryById(Long id) {
        return this.notificationDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Notification> queryAllByLimit(int offset, int limit) {
        return this.notificationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param notification 实例对象
     * @return 实例对象
     */
    @Override
    public Notification insert(Notification notification) {
        this.notificationDao.insert(notification);
        return notification;
    }

    /**
     * 修改数据
     *
     * @param notification 实例对象
     * @return 实例对象
     */
    @Override
    public Notification update(Notification notification) {
        this.notificationDao.update(notification);
        return this.queryById(notification.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.notificationDao.deleteById(id) > 0;
    }

    @Override
    public Integer queryAllCount(Notification notification) {
        return notificationDao.queryAllCount(notification);
    }


    @Override
    public PageInfo<Notification> list(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Notification notificationExp = new Notification();
        notificationExp.setReceiver(userId);
        List<Notification> list = notificationDao.queryAll(notificationExp);
        PageInfo<Notification> pageInfo = new PageInfo<Notification>(list);
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
}