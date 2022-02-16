/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.season;

import domain.Season;
import java.sql.Connection;
import project.repository.Repository;

import project.repository.db.impl.SeasonRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class UpdateSeasonSO extends AbstractSO{
     private final Repository seasonStorrage;

    public UpdateSeasonSO() {
        seasonStorrage = new SeasonRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if(!(new SeasonRepository().updateFirstObject(entity) instanceof Season) || !(new SeasonRepository().updateSecondPrimitive(entity) instanceof String))
            throw new ValidationException("Podaci nisu validni");
        
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            seasonStorrage.eddit(new SeasonRepository().updateFirstObject(entity),new SeasonRepository().updateSecondPrimitive(entity),conn);
           return true;
        } catch (Exception e) {
            throw new Exception("Sezona ne moze da se izmeni");
        }
    }

   @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) seasonStorrage.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        seasonStorrage.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        seasonStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        seasonStorrage.disconnect(conn);
    }
}
