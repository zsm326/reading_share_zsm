package com.read.filter;

import com.read.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录拦截过滤器：
 * 1. 统一设置请求编码UTF-8
 * 2. 未登录用户访问受保护资源时，跳转到登录页
 * 3. admin_ 开头的后台功能仅管理员可访问
 */
public class LoginFilter implements Filter {

    /** 游客也可访问的action（无需登录） */
    private static final List<String> PUBLIC_ACTIONS = Arrays.asList(
            "user_login", "user_register",
            "book_list", "book_detail",
            "booklist_public", "booklist_detail",
            "note_list", "note_detail"
    );

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 统一处理POST中文乱码
        req.setCharacterEncoding("UTF-8");

        // 取得action名，例如 /user_login.action -> user_login
        String path = req.getServletPath();
        String actionName = path.substring(1, path.lastIndexOf(".action"));

        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 公开资源直接放行
        if (PUBLIC_ACTIONS.contains(actionName)) {
            chain.doFilter(request, response);
            return;
        }

        // 未登录 -> 跳转登录页
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/pages/user/login.jsp");
            return;
        }

        // 后台管理功能仅管理员可访问
        if (actionName.startsWith("admin_") && loginUser.getRole() != 1) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
