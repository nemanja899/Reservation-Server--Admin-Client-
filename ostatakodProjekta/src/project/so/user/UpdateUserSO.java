/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.user;

import domain.User;
import java.sql.Connection;
import project.repository.Repository;

import project.repository.db.impl.UserRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class UpdateUserSO extends AbstractSO{
    private final Repository userStorrage;

    public UpdateUserSO() {
        userStorrage = new UserRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if(!(new UserRepository().updateFirstObject(entity) instanceof User)|| !(new UserRepository().updateSecondPrimitive(entity) instanceof String))
            throw new ValidationException("Podaci nisu validni");
        
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            userStorrage.eddit(new UserRepository().updateFirstObject(entity),new UserRepository().updateSecondPrimitive(entity),conn);
           return true;
        } catch (Exception e) {
            throw new Exception("Korisnik ne moze da se izmeni");
        }
    }

   @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) userStorrage.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        userStorrage.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        userStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        userStorrage.disconnect(conn);
    }
}
