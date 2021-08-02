package com.example.email_j2ee.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/welcome", "/inbox", "/compose", "/draft", "/sent", "/logout"})
public class AuthenticateFilter extends HttpFilter {

    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req.getSession(false)==null||req.getSession(false).getAttribute("authenticated")==null||!(boolean)req.getSession(false).getAttribute("authenticated")) {
            res.sendRedirect("hello?error=please login or register");
            return;
        }
            chain.doFilter(req, res);
    }
}
