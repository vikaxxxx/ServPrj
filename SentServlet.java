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

@WebServlet(value = "/sent")
public class SentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        Connection connection= (Connection) request.getServletContext().getAttribute("connection");
        try {
            Statement statement= connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from mail where sender_email='"+request.getSession(false).getAttribute("email")+"'");
            List<Message> l= new ArrayList();
            while (resultSet.next()){
                Message message =new Message(resultSet.getInt("id"), resultSet.getString("receiver_email"),resultSet.getString("message"));
                l.add(message);
            }

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>sent</h1>");
            for(Message m:l) {
                out.println("to: " + m.email+" <br/>");
                out.println("message: " + m.message+" <br/>");
                out.println("<button onClick=\"fetch('http://localhost:8080/email_j2ee_war_exploded/sent?messageId="+m.id+"', {method: 'DELETE'})\">delete</button><br/><br/>");
//            out.println("<a href=\"compose?email="+m.by+"\"><button>reply</button><a/><br/><br/><br/><br/>");
            }
            out.println("<hr><a href=\"compose\"><button>compose</button></a>");
            out.println("<a href=\"draft\"><button>draft</button></a>");
            out.println("<a href=\"inbox\"><button>inbox</button></a>");
            out.println("<a href=\"logout\"><button style=\"color:red\">logout</button></a>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("sent?error=oops somethin went wrong");
            return;
        }



    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection connection= (Connection) request.getServletContext().getAttribute("connection");
        String id = request.getParameter("messageId");
        try {
            Statement statement= connection.createStatement();
            statement.execute("delete from mail where id="+id);
//            response.sendRedirect("http://localhost:8080/email_j2ee_war_exploded/sent");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("sent?error=oops somethin went wrong");
            return;
        }
    }

}
