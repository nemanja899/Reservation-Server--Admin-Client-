/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db;

import java.sql.SQLException;
import project.repository.Repository;
import java.sql.Connection;
import project.repository.db.connectionpool.DbConnectionPool;


/**
 *
 * @author User
 */
public interface DbRepository<T,E,K> extends Repository<T,E,Connection>{
    
    @Override
    public default Connection  connect()throws SQLException{
       return DbConnectionPool.getInstance().getConnection();
    }
    @Override
    public default void disconnect(Connection conn) throws SQLException{
        DbConnectionPool.getInstance().releseConnection(conn);
    }
    @Override
    public default void commit(Connection conn) throws SQLException{
        conn.commit();
    }
    @Override
    public default void rollback(Connection conn) throws SQLException{
        conn.rollback();
    }
}
