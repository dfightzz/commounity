package cn.dzz.community.controller;

import cn.dzz.community.entity.Comment;
import cn.dzz.community.entity.Notification;
import cn.dzz.community.model.Question;
import cn.dzz.community.service.CommentService;
import cn.dzz.community.service.NotificationService;
import cn.dzz.community.service.QuestionService;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentService commentService;

    @RequestMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          @CookieValue("token") String token,
                          HttpServletRequest request,
                          Integer pageNum,
                          Integer pageSize) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PageInfo<Question> pageInfo = questionService.list(user.getId(), pageNum == null ? 1 : pageNum, 5);
            model.addAttribute("pageInfo", pageInfo);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
            PageInfo<Notification> pageInfo = notificationService.list(user.getId(), pageNum == null ? 1 : pageNum, 5);
            model.addAttribute("pageInfo", pageInfo);
        }

        return "profile";
    }

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") Long id){
        Notification notification = notificationService.queryById(id);
        notification.setStatus(1);
        notificationService.update(notification);
        if (notification.getType() == 0) {
            return "redirect:/question/" + notification.getOuterId();
        } else {
            Long outerId = notification.getOuterId();
            Comment comment = commentService.queryById(outerId);
            return "redirect:/question/" + comment.getParentId();
        }
    }
}
