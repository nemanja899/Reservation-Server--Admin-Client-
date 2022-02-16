/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class DbConnectionFactory {
    private Connection conn;
    private static DbConnectionFactory instance;
    
    private DbConnectionFactory(){
    }
    public static DbConnectionFactory getInstance(){
        if(instance==null){
            instance = new DbConnectionFactory();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException{
         try {
            if(conn==null || conn.isClosed()){
                String url="jdbc:mysql://localhost:3306/dbKomercijalniLov";
                String user="root";
                String password="admin";
               conn =  DriverManager.getConnection(url, user, password);
               conn.setAutoCommit(false);
                System.out.println("Uspesna konekcija");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Konekcija sa bazom je neuspesna");
            throw ex;
        }
         return conn;
    }
}
