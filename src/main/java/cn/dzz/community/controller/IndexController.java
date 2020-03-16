package cn.dzz.community.controller;


import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class IndexController {

    @Autowired
    private UserInterface userInterface;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                User user = userInterface.findUserByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        return "index";
    }


}
