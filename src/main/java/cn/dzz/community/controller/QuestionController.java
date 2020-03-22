package cn.dzz.community.controller;

import cn.dzz.community.model.Question;
import cn.dzz.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/question/{id}")
    public String questionDetail(@PathVariable(name = "id") Integer id, Model model){
        Question question = questionService.getQuestionById(id);
        model.addAttribute("question",question);
        return "questionDetail";
    }
}
