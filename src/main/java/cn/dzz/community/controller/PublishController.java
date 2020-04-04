package cn.dzz.community.controller;

import cn.dzz.community.mapper.QuestionMapper;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserInterface userInterface;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id")Integer id, Model model) {
        Question question = questionMapper.getQuestionById(id);

        model.addAttribute("id", question.getId());
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());

        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question,
                            Model model,
                            @CookieValue("token") String token,
                            HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());

        if (StringUtils.isEmpty(question.getTitle())) {
            model.addAttribute("error", "标题不能为空!");
            return "publish";
        }
        if (StringUtils.isEmpty(question.getDescription())) {
            model.addAttribute("error", "描述不能为空!");
            return "publish";
        }
        if (StringUtils.isEmpty(question.getTag())) {
            model.addAttribute("error", "标签不能为空!");
            return "publish";
        }
        question.setGmtModified(System.currentTimeMillis());
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setCreator(user.getId());
            question.setViewCount(0);
            question.setCommitCount(0);
            questionMapper.create(question);
        } else {
            if (user.getId() != questionMapper.getQuestionById(question.getId()).getCreator()) {
                model.addAttribute("error", "您不是此问题的作者，无法修改。");
                return "publish";
            }
            questionMapper.update(question);
        }
        return "redirect:/";
    }
}
