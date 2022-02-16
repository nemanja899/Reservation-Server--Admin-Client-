/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.user;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.UserRepository;

import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllUserSO extends AbstractSO {

    private final Repository userStorrage;

    public GetAllUserSO() {
        userStorrage = new UserRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        // nema preduslova
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            return userStorrage.getAll(conn);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Korisnici ne mogu da se ucitaju");
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
