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
public class UpdateAnimalSO extends AbstractSO {

    private final Repository animalStorrage;
    
    public UpdateAnimalSO() {
        animalStorrage = new AnimalRepository();
    }
    
    @Override
    protected void precondition(Object entity) throws ValidationException {        
        Object[] obj = (Object[]) entity;
        if (!(obj[0] instanceof Animal) || !(obj[1] instanceof Long)) {
            throw new ValidationException("Nije zivotinja ili podaci nisu validni");
        }
        Animal param = (Animal) obj[0];
        Long id = (Long) obj[1];
        if (param.getName().isEmpty() || param.getShortName().isEmpty() || id < 0) {
            throw new ValidationException("Podaci zivotinje nisu validni");
        }
    }
    
    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           Animal animal= ((AnimalRepository)animalStorrage).updateFirstObject(entity);
           Long id= ((AnimalRepository)animalStorrage).updateSecondPrimitive(entity);
            animalStorrage.eddit(animal, id, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Zivotinja nije izmenjena");
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
