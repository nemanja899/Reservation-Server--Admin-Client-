/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.Hunter;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.HunterRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class SearchHunterSO extends AbstractSO {

    private final Repository hunterStorrage;

    public SearchHunterSO() {
        hunterStorrage = new HunterRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
       if(!(entity instanceof String))
           throw new ValidationException("Padaci nisu validni pogresan podatak");
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            return hunterStorrage.search(entity,conn);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lovac ne moze da se ponadje");
        }
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        hunterStorrage.commit(conn);
    }

    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) hunterStorrage.connect();
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        hunterStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        hunterStorrage.disconnect(conn);
    }
}
