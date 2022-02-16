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
import validation.Validator;

/**
 *
 * @author User
 */
public class AddUserSO extends AbstractSO{
         private final Repository userStorrage;

    public AddUserSO() {
        userStorrage = new UserRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof User)) {
            throw new ValidationException("Nije korisnik");
        }
    
              Validator.startValidate()
                .validateInputPattern(((User)entity).getEmail(), "^[a-z-A-Z0-9]+@.+$", "Email ne sadrzi @")
                .validateInputPattern(((User)entity).getPassword(),"^[a-zA-Z]+[0-9]+[a-zA-Z0-9]+$", "Sifra mora da sadrzi slovo i broj i ne moze da pocne brojem")
                .throwIfInvalide();
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            userStorrage.add((User) entity, conn);
            return true;
        } catch (Exception e) {
            throw new Exception("Korinsik ne moze da se doda");
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
