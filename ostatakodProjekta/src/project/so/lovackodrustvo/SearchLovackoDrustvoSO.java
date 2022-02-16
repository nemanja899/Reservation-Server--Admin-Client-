/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.lovackodrustvo;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.LovackoDrustvoRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class SearchLovackoDrustvoSO extends AbstractSO {

    private final Repository drustvoRepository;

    public SearchLovackoDrustvoSO() {
        drustvoRepository = new LovackoDrustvoRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof String)) {
            throw new ValidationException("Potrebno je uneti ime drustva");
        }
        String ime = (String) entity;
        if (ime.length() < 5) {
            throw new ValidationException("Ime drustva mora imati vise od 4 slova");
        }

    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            return drustvoRepository.search(entity, conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Drustvo nije pronadjeno");
        }

    }

    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) drustvoRepository.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        drustvoRepository.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        drustvoRepository.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        drustvoRepository.disconnect(conn);
    }

}
