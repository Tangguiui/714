package com.oa5.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        // 请求上下文路径
        String contextPath = request.getContextPath();
        // 当前正在访问的URL，要传给登录页面，以便登录成功后跳转到该页面，该URL要进行编码处理
        String currUrl = java.net.URLEncoder.encode(request.getServletPath().toString(), "UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            // 拼接登录URL
            String loginUrl = contextPath + "/";
            // 响应重定向到登录URL
            response.sendRedirect(loginUrl);
            // 返回false，说明被拦截
            return false;
        }
        return true;
    }
}
