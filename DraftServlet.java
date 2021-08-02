package com.example.email_j2ee.servlet;

import com.example.email_j2ee.entity.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(value = "/draft")
public class DraftServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        Connection connection= (Connection) request.getServletContext().getAttribute("connection");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from draft where user_email='" + request.getSession(false).getAttribute("email") + "'");
            List<Message> l = new ArrayList();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("id"), resultSet.getString("receiver_email"), resultSet.getString("message"));
                l.add(message);
            }
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>draft</h1>");
            for (Message m : l) {
                out.println("<form action=\"compose\" method=\"post\">");
                out.println("<label> to:</label><br>");
                out.println("<input id=\"email\" type=\"email\" name=\"email\" value=\"" + m.email + "\"><br><br>");
                out.println("<label> message:</label><br>");
                out.println("<textarea id=\"message\" type=\"text\" rows=\"4\" cols=\"50\" name=\"message\">" + m.message + "</textarea><br><br>");
                out.println("<input hidden name=\"draftId\" value=\"" + m.id + "\">");
                out.println("<input type=\"submit\" value=\"send\">");
                out.println("</form>");
                out.println("<button onClick=\"fetch(`http://localhost:8080/email_j2ee_war_exploded/draft?draftId="+m.id+"&email=${document.getElementById('email').value}&message=${document.getElementById('message').value}`,{method: 'PUT'})\">save</button>");
                out.println("<button onClick=\"fetch('http://localhost:8080/email_j2ee_war_exploded/draft?draftId="+m.id+"', {method: 'DELETE'})\">delete</button><br/><br/><br/><br/><br/>");

            }
            out.println("<hr><a href=\"inbox\"><button>inbox</button></a>");
            out.println("<a href=\"compose\"><button>compose</button></a>");
            out.println("<a href=\"sent\"><button>sent</button></a>");
            out.println("<a href=\"logout\"><button style=\"color:red\">logout</button></a>");
            out.println("</body></html>");
        }catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("inbox?error=oops somethin went wrong");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email=request.getParameter("email");
        String message= request.getParameter("message");
        Connection connection= (Connection) request.getServletContext().getAttribute("connection");

        try {
            Statement statement = connection.createStatement();
            statement.execute("insert into draft (message, receiver_email, user_email) values('"+message+"', '"+email+"', '"+request.getSession(false).getAttribute("email")+"');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect("draft");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email=request.getParameter("email");
        String message= request.getParameter("message");
        String draftId= request.getParameter("draftId");
        Connection connection= (Connection) request.getServletContext().getAttribute("connection");

        try {
            Statement statement = connection.createStatement();
            PrintWriter out = response.getWriter();
            statement.execute("update draft set message='"+message+"', receiver_email='"+email+"' where id="+draftId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inbox?error=oops somethin went wrong");
            return;
        }
        response.sendRedirect("draft");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection= (Connection) request.getServletContext().getAttribute("connection");
        String id = request.getParameter("draftId");
        try {
            Statement statement= connection.createStatement();
            statement.execute("delete from draft where id="+id);

            response.sendRedirect("compose");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inbox?error=oops somethin went wrong 505");
            return;
        }
    }
}
