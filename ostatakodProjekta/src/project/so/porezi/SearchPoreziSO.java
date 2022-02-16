/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.porezi;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.PoreziRepository;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class SearchPoreziSO extends AbstractSO{
       private final Repository poreziRepository;

    public SearchPoreziSO() {
        poreziRepository = new PoreziRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof String)) {
            throw new ValidationException("Potrebno je uneti sezonu");
        }
        String season = (String) entity;
          Validator.startValidate()
                   .validateSeasonString(season)
                  .throwIfInvalide();

    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            return poreziRepository.search(entity, conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("porez nije pronadjen");
        }

    }

    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) poreziRepository.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        poreziRepository.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        poreziRepository.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        poreziRepository.disconnect(conn);
    }
}
