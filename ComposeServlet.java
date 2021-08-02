package com.example.email_j2ee.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(value = "/compose")
public class ComposeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>compose a mail</h1>");
        if (request.getParameter("error") != null)
            out.println("<h4 style=\"color:red\">" + request.getParameter("error") + "</h4>");
        out.println("<form action=\"compose\" method=\"post\">");
        out.println("<label> to:</label><br>");
        if (request.getParameter("email") != null)
            out.println("<input id=\"email\" type=\"email\" name=\"email\" value=\"" + request.getParameter("email") + "\"><br><br>");
        else
            out.println("<input id=\"email\" type=\"email\" name=\"email\"><br><br>");
        out.println("<label> message:</label><br>");
        out.println("<textarea id=\"message\" type=\"text\" rows=\"4\" cols=\"50\" name=\"message\"></textarea><br><br>");
        out.println("<input type=\"submit\" value=\"send\">");
        out.println("</form>");
        out.println("<button onClick=\"fetch(`http://localhost:8080/email_j2ee_war_exploded/draft?email=${document.getElementById('email').value}&message=${document.getElementById('message').value}`,{method: 'POST'})\">save as draft</button>");
        out.println("<hr><a href=\"inbox\"><button>inbox</button></a>");
        out.println("<a href=\"draft\"><button>draft</button></a>");
        out.println("<a href=\"sent\"><button>sent</button></a>");
        out.println("<a href=\"logout\"><button style=\"color:red\">logout</button></a>");
        out.println("</body></html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        String draftId= request.getParameter("draftId");
        Connection connection = (Connection) request.getServletContext().getAttribute("connection");




        try {
            Statement statement = connection.createStatement();
            if(draftId!=null)
            statement.execute("delete from draft where id="+draftId);
            ResultSet resultSet = statement.executeQuery("select * from user where email='" + email + "'");
            if (!resultSet.next()) {
                response.sendRedirect("compose?error=email " + email + " doesn't exist");
                return;
            }
            statement.execute("insert into mail (message, sender_email,receiver_email ) values('" + message + "','" + request.getSession(false).getAttribute("email") + "','" + email + "')");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("compose?error=oops somethin went wrong");
            return;
        }

        response.sendRedirect("sent");
    }
}
