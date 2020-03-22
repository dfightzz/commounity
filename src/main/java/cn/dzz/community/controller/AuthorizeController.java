package cn.dzz.community.controller;

import cn.dzz.community.dto.GithubAccessToken;
import cn.dzz.community.dto.GithubUser;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.User;
import cn.dzz.community.provider.GithubProvider;
import cn.dzz.community.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value(("${github.client.secret}"))
    private String clientSecret;

    @Value(("${github.redirect.uri}"))
    private String redirectUri;

    @Autowired
    private UserInterface userInterface;

    @GetMapping("callback")
    public String callback(@RequestParam String code,
                           @RequestParam String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        GithubAccessToken githubAccessToken = new GithubAccessToken();
        githubAccessToken.setClient_id(clientId);
        githubAccessToken.setClient_secret(clientSecret);
        githubAccessToken.setCode(code);
        githubAccessToken.setRedirect_uri(redirectUri);
        githubAccessToken.setState(state);
        // 获取token
        String acceessToken = githubProvider.getAcceessToken(githubAccessToken);
        // 根据token获取User信息
        GithubUser user = githubProvider.getUser(acceessToken);
        if (user != null) {
            // 认证通过
            User insertUser = new User();
            insertUser.setAccountId(String.valueOf(user.getId()));
            insertUser.setLogin(user.getLogin());
            insertUser.setName(user.getName());
            String token = String.valueOf(UUID.randomUUID());
            insertUser.setToken(token);
            insertUser.setGmtCreated(System.currentTimeMillis());
            insertUser.setGmtModified(System.currentTimeMillis());
            insertUser.setAvatarUrl(user.getAvatarUrl());
            // 判断同一github的用户是否登录过
            User oldUser = userInterface.findUserByAccountId(insertUser.getAccountId());
            if (oldUser != null) {
                insertUser.setId(oldUser.getId());
                insertUser.setGmtCreated(oldUser.getGmtModified());
                userInterface.updateUser(insertUser);
            } else {
                userInterface.insertUser(insertUser);
            }
            // 将token写入cookie， 用以做登录状态的检查
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            // 认证未通过
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        Cookie   killMyCookie   =   new   Cookie("token",   null);
        killMyCookie.setMaxAge(0);
        killMyCookie.setPath("/");
        response.addCookie(killMyCookie);
        request.getSession().invalidate();
        return "redirect:/";
    }
}
