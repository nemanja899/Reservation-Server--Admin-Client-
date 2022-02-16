/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.databasebroker;

import java.util.List;

/**
 *
 * @author User
 */
public interface IBrokerBazePodataka<T, E, K> {

    List<T> getAll(T param, K connection) throws Exception;

    List<T> getAllByCondition(T param, Object condition, K connection) throws Exception;

    void add(T odk, K connection) throws Exception;

    void eddit(T odk, E oldPk, K connection) throws Exception;

    void delete(T odk, K connection) throws Exception;

    void deleteByPkParrent(T odk, Object condition, K connection) throws Exception;

    T search(T odk, E param, K connection) throws Exception;

    K connect() throws Exception;

    void disconnect(K connection) throws Exception;

    void commit(K connection) throws Exception;

    void rollback(K conncection) throws Exception;

}
