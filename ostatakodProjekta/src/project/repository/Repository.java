/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository;

import java.util.List;
//import com.mycompany.repository.db.DbConnectionFactory;

/**
 *
 * @author User
 */
public interface Repository<T, E, K> {

    List<T> getAll(K connection) throws Exception;

    void add(T param,K connection) throws Exception;

    void eddit(T param, E oldPk,K connection) throws Exception;

    void delete(T param,K connection) throws Exception;

    T search(E param,K connection) throws Exception;

    K connect() throws Exception;

    void disconnect(K connection) throws Exception;

    void commit(K connection) throws Exception;

    void rollback(K conncection) throws Exception;

    

}
