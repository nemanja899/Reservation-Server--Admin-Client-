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
public class GetAllAnimalsSO extends AbstractSO {

    private final Repository animalStorrage;

    public GetAllAnimalsSO() {
        animalStorrage = new AnimalRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        //nema ogranicenja ni vrednosnih ni strukturnih
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return animalStorrage.getAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Zivotinje ne mogu da se ucitaju");
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
