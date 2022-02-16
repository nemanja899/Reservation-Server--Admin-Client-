/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.user;

import domain.User;
import domain.User;
import java.sql.Connection;

import project.repository.db.impl.UserRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class DeleteUserSO extends AbstractSO{
          private final UserRepository userStorrage;

    public DeleteUserSO() {
        userStorrage = new UserRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
       if (!(entity instanceof User)) {
            throw new ValidationException("Nije korisnik");
        }
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            userStorrage.delete((User) entity, conn);
            return true;
        } catch (Exception e) {
            throw new Exception("Korisnik ne moze da se obrise");
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
