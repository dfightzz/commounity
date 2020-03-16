package cn.dzz.community.controller;

import cn.dzz.community.dto.GithubAccessToken;
import cn.dzz.community.dto.GithubUser;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.User;
import cn.dzz.community.provider.GithubProvider;
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
        String acceessToken = githubProvider.getAcceessToken(githubAccessToken);
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
//            request.getSession().setAttribute("user", user);
            userInterface.insertUser(insertUser);
            // 将token写入cookie， 用以做登录状态的检查
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            // 认证未通过
            return "redirect:/";
        }

    }
}
