/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.animal;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.AnimalRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class SearchAnimalSO extends AbstractSO {

    private final Repository animalStorrage;

    public SearchAnimalSO() {
        animalStorrage = new AnimalRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof Long)) {
            throw new ValidationException("Nije validan podatak");
        }
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return animalStorrage.search(entity, conn);
             
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Zivotinja nije nadjena");
            
        }
    }

    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) animalStorrage.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        animalStorrage.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        animalStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        animalStorrage.disconnect(conn);
    }

}
