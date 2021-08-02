package com.example.email_j2ee;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc= sce.getServletContext();
        try {
            DBManager dbManager= new DBManager("jdbc:mysql://localhost:3306/email","root","root");
            Connection connection= dbManager.getConnection();
            sc.setAttribute("connection", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext= sce.getServletContext();
        if(servletContext.getAttribute("connection")!=null) {
            Connection connection = (Connection) servletContext.getAttribute("connection");
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
