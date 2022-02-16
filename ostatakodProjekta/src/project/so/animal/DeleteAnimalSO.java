/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.animal;

import domain.Animal;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.AnimalRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class DeleteAnimalSO extends AbstractSO {

    private final Repository animalStorrage;

    public DeleteAnimalSO() {
        animalStorrage = new AnimalRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (entity == null || !(entity instanceof Animal)) {
            throw new ValidationException("Nije zivotinja");
        }
        Animal animal = (Animal) entity;
        //provera vrednosnih ogranicenja
        if (animal.getShortName().isEmpty()) {
            throw new ValidationException("Ime je prazno...");
        }
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            animalStorrage.delete((Animal) entity, conn);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Zivotinja ne moze da se obrise");
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
