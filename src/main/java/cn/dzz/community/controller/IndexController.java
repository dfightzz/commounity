package cn.dzz.community.controller;


import cn.dzz.community.service.QuestionService;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.Question;
import cn.dzz.community.model.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Integer pageNum, Integer pageSize, Model model){

        PageInfo<Question> pageInfo = questionService.list( null, pageNum == null? 1:pageNum, 5);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }
}
