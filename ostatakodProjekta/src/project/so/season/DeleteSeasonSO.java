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
public class DeleteSeasonSO extends AbstractSO{
      private final Repository seasonStorrage;

    public DeleteSeasonSO() {
        seasonStorrage = new SeasonRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof Season)) {
            throw new ValidationException("Nije Sezona");
        }
        Season season=(Season) entity;
        if(season.getSeason().length()>9)
            throw new ValidationException("Podaci nisu validni");
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            seasonStorrage.delete((Season) entity, conn);
            return true;
        } catch (Exception e) {
            throw new Exception("Sezona ne moze da se obrise");
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
