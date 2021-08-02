package com.example.email_j2ee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private Connection connection;

    public DBManager(String dbURL, String user, String pwd) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(dbURL, user, pwd);
        System.out.println("connected");
    }

    public Connection getConnection(){
        return this.connection;
    }
}
