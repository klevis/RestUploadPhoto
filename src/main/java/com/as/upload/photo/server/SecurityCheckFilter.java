package com.as.upload.photo.server;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SecurityCheckFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest= (HttpServletRequest) req;
        HttpSession session = httpServletRequest.getSession(false);
        System.out.println("session = " + session);
        if(session!=null)  {
        chain.doFilter(req, resp);
        }
        resp.getWriter().println("You must authenticate first!Please call http://IP:PORT/authenticate");
    }
    private  FilterConfig config;
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

}
