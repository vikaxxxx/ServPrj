package com.example.email_j2ee.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<html><body>");
        out.println("<h1>Login</h1>");
        if(req.getParameter("error")!=null)
            out.println("<h4 style=\"color:red\">"+req.getParameter("error")+"</h4>");
        out.println( "<form action=\"login\" method=\"post\">");
        out.println("<label> email:</label><br>");
        out.println("<input type=\"email\" name=\"email\"><br><br>");
        out.println("<label> password:</label><br>");
        out.println("<input type=\"password\" name=\"password\"><br><br>");
        out.println("<input type=\"submit\" value=\"log in\">");
        out.println( "</form>");
        out.println("<a href=\"register\"><button style=\"background-color:green\">register</button></a>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        String email =req.getParameter("email");
        String password= req.getParameter("password");

        Connection connection= (Connection) req.getServletContext().getAttribute("connection");
        try {
            Statement statement = connection.createStatement();
          ResultSet resultSet= statement.executeQuery("select * from user where email='"+email+"' AND password='"+password+"';");
          //check if valid
          if(!resultSet.next()){
              res.sendRedirect("login?error=invalid credentials");
              return;
          }
            HttpSession httpSession= req.getSession();
            httpSession.setAttribute("authenticated", true);
            httpSession.setAttribute("email",req.getParameter("email") );
            httpSession.setAttribute("name",resultSet.getString("name") );
            res.sendRedirect("welcome");
        }catch (Exception e){
            {
                res.sendRedirect("login?error=oops somthin went wrong");
                return;
            }
        }




    }
}
