/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.connectionpool;

/**
 *
 * @author User
 */
import java.sql.Connection;

public class ConnectionClone {

    private Connection connection;

    public ConnectionClone(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
    
    
}
