package com.fyp.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NonAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object currentUser = request.getSession().getAttribute("type");
        if (!currentUser.toString().equals("class com.fyp.pojo.User")){
            request.setAttribute("msg","您的身份无法浏览此页面");
            request.getRequestDispatcher("/illegalvisit.html").forward(request,response);
            return false;
        }else {
            return true;
        }
    }
}
