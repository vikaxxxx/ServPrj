package com.example.email_j2ee.servlet;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<html><body>");
        out.println("<h1>Register</h1>");
        if(req.getParameter("error")!=null)
        out.println("<h4 style=\"color:red\">"+req.getParameter("error")+"</h4>");
        out.println( "<form action=\"register\" method=\"post\">");
        out.println("<label> email:</label><br>");
        out.println("<input type=\"email\" name=\"email\"><br><br>");
        out.println("<label> name:</label><br>");
        out.println("<input type=\"name\" name=\"name\"><br><br>");
        out.println("<label> password:</label><br>");
        out.println("<input type=\"password\" name=\"password\"><br><br>");
        out.println("<label> retype-password:</label><br>");
        out.println("<input type=\"password\" name=\"retypePassword\"><br><br>");
        out.println("<input type=\"submit\" value=\"register\">");
        out.println( "</form>");
        out.println("<a href=\"login\"><button style=\"background-color:blue\">login</button></a>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email =req.getParameter("email");
        String name =req.getParameter("name");
        String password= req.getParameter("password");

        Connection connection= (Connection) req.getServletContext().getAttribute("connection");
        try {
            Statement statement = connection.createStatement();
            statement.execute("insert into user (email,name,password) values ('"+email+"','"+name+"','"+password+"');");
        }catch (SQLIntegrityConstraintViolationException e){
            {
                res.sendRedirect("register?error=email already exist");
                return;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect("register?error=oops somethin went wrong");
            return;
        }


        HttpSession  httpSession= req.getSession();
        httpSession.setAttribute("authenticated", true);
        httpSession.setAttribute("email",email);
        httpSession.setAttribute("name",name);
        res.sendRedirect("welcome");


    }
}
