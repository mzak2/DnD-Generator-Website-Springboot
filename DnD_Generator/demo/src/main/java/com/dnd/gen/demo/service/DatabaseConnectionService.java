package com.dnd.gen.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DatabaseConnectionService {
    private final String dbUrl;
    private final String dbUser;

    private final String dbPass;

    public DatabaseConnectionService(@Value("${DB_URL}") String dbUrl,
                                     @Value("${DB_USER}") String dbUser,
                                     @Value("${DB_PASS}") String dbPass){
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
}
