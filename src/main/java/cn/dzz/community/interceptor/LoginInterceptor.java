package cn.dzz.community.interceptor;

import cn.dzz.community.entity.Notification;
import cn.dzz.community.mapper.UserInterface;
import cn.dzz.community.model.User;
import cn.dzz.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private NotificationService notificationService;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 当当前用户未登录时回去查找用户是否存在token

        if ((User) request.getSession().getAttribute("user") == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        User user = userInterface.findUserByToken(token);
                        // 若此用户被验证存在，则将用户放入session中
                        if (user != null) {
                            request.getSession().setAttribute("user", user);
                            Notification notification = new Notification();
                            notification.setStatus(0);
                            notification.setReceiver(user.getId());
                            int count = notificationService.queryAllCount(notification);
                            request.getSession().setAttribute("unReadCount", count);
                        }
                        break;
                    }
                }
            }
        } else {
            User user = (User) request.getSession().getAttribute("user");
            Notification notification = new Notification();
            notification.setStatus(0);
            notification.setReceiver(user.getId());
            int count = notificationService.queryAllCount(notification);
            request.getSession().setAttribute("unReadCount", count);
        }
        return true;
    }
}
