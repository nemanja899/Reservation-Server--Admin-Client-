/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class DbConnectionFactory {
    private Connection conn;
   
    
    public DbConnectionFactory(){
    }
  
    
    public Connection getConnection(String username,String password,String ur) throws SQLException{
         try {
            if(conn==null || conn.isClosed()){
               // mysql://localhost:3306
                String url="jdbc:"+ur+"/dbKomercijalniLov";
                //String user="root";
                //String password="admin";
               conn =  DriverManager.getConnection(url, username, password);
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
