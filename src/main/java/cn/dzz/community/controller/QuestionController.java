package cn.dzz.community.controller;

import cn.dzz.community.model.Question;
import cn.dzz.community.service.QuestionService;
import cn.dzz.community.service.QuestionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionService2 questionService2;

    @RequestMapping("/question/{id}")
    public String questionDetail(@PathVariable(name = "id") Integer id, Model model){
        Question question = questionService.getQuestionById(id, 0);
        questionService.incView(id);
        model.addAttribute("question",question);
        List<Question> relativeList =  questionService.selectRelive(question);
        model.addAttribute("relativeList",relativeList);
        return "questionDetail";
    }
    @RequestMapping("/questionTag/{id}")
    @ResponseBody
    public Question questionTag(@PathVariable(name = "id") Integer id, Model model){
        return questionService2.queryById(id);
    }
}
