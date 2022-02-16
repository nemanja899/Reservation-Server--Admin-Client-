/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.Hunter;

import domain.Hunter;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.HunterRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllHunterSO extends AbstractSO{
      private final Repository hunterStorrage;

    public GetAllHunterSO() {
        hunterStorrage = new HunterRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        //
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return hunterStorrage.getAll(conn);
             
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lovci ne mogu da se ucitaju");
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
