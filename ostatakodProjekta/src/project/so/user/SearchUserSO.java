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
public class SearchUserSO extends AbstractSO{
        private final Repository userStorrage;

    public SearchUserSO() {
        userStorrage = new UserRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
       
        if(!(entity instanceof String) )
            throw new ValidationException("Podaci nisu validni");
           Validator.startValidate()
                .validateInputPattern((String) entity, "^[a-z-A-Z0-9]+@.+$", "Email mora biti u formatu ***@**")
                .throwIfInvalide();
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return(User) userStorrage.search(entity,conn);
         
           
        } catch (Exception e) {
            throw new Exception("User ne postoji");
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
