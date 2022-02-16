/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so;

import domain.GeneralDObject;
import validation.ValidationException;
import java.sql.Connection;
import project.repository.databasebroker.BrokerBazePodataka;
import project.repository.connectionpool.DbConnectionPool;

/**
 *
 * @author User
 */
public abstract class AbstractSO {

    static public BrokerBazePodataka bbp = new BrokerBazePodataka();

    public Object execute(GeneralDObject entity,Object condition) throws Exception {
        Connection conn = null;
        Object result = null;
        try {

            precondition(entity,condition);
            conn = bbp.connect();
            result = executeOperation(entity,condition, conn);
            bbp.commit(conn);
             return result;
        } catch (Exception e) {
            if (conn != null) {
                bbp.rollback(conn);
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());

        } finally {
            if (conn != null) {
                bbp.disconnect(conn);
            }

        }
       
    }

    protected abstract void precondition(GeneralDObject entity,Object condition) throws ValidationException;

    protected abstract Object executeOperation(GeneralDObject entity,Object condition, Connection conn) throws Exception;

    
}
