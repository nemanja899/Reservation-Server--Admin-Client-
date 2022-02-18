/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.connectionpool;

import java.io.FileInputStream;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utill.MyConstants;

/**
 *
 * @author User
 */
public class DbConnectionPool extends Thread {

    private static DbConnectionPool instance;
    private List<Connection> connectionPool;
    private List<Connection> activeConnections;
    private String username;
    private String password;
    private String url;

    private DbConnectionPool() {
        connectionPool = new ArrayList<>();
        activeConnections = new ArrayList<>();
        try {
            Properties prop = new Properties();
         
            prop.load(DbConnectionPool.class.getClassLoader().getResourceAsStream(MyConstants.DATA_BASE_FILE_NAME));
            username = prop.getProperty(MyConstants.USERNAME);
            password = prop.getProperty(MyConstants.PASSWORD);
            url=prop.getProperty(MyConstants.URL);
            System.out.println(url);
       
        } catch (Exception e) {
            e.printStackTrace();
        }

        start();
    }

    @Override
    public void run() {

        for (int i = 20; i > 0; i--) {
            try {
                Connection connectionClone = new DbConnectionFactory().getConnection(username,password,url);
                connectionPool.add(connectionClone);
                System.out.println(connectionClone);
            } catch (SQLException ex) {
                Logger.getLogger(DbConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void releseConnection(Connection conn) {
        activeConnections.remove(conn);
        connectionPool.add(0, conn);

        //  System.out.println("Konekcija je vracena");
        // System.out.println("Aktivnih ima: "+activeConnections.size());
        // System.out.println("Neaktivnih  ima: "+connections.size());
    }

    public static DbConnectionPool getInstance() {
        if (instance == null) {
            instance = new DbConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            if (connectionPool.isEmpty()) {

                return new DbConnectionFactory().getConnection(username,password,url);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        Connection conn = connectionPool.remove(connectionPool.size() - 1);
        activeConnections.add(conn);
        // System.out.println("Konekcija je uzeta....");
        // System.out.println("Aktivnih ima: "+activeConnections.size());
        // System.out.println("Neaktivnih  ima: "+connections.size());
        return conn;
    }

    public void closeAllConnections() {
        connectionPool.stream().forEach(c -> {
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public List<Connection> getActiveConnections() {
        return activeConnections;
    }

    public void closeAllActiveConnections() {
        activeConnections.stream().forEach((c) -> {
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void releseActiveConnections() {
        if (!activeConnections.isEmpty()) {
            activeConnections.stream().forEach((c) -> {
                connectionPool.add(0, c);
            });
        } else {
            System.out.println("Nema aktivnih konekcija");
        }
    }


}
