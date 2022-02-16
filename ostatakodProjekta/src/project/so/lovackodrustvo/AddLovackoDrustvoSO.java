/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.lovackodrustvo;

import domain.LovackoDrustvo;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.AnimalRepository;
import project.repository.db.impl.LovackoDrustvoRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class AddLovackoDrustvoSO extends AbstractSO {
    
    private final Repository drustvoRepository;
    
    public AddLovackoDrustvoSO() {
        drustvoRepository = new LovackoDrustvoRepository();
    }
    
    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (entity == null || !(entity instanceof LovackoDrustvo)) {
            throw new ValidationException("Nije lovacko drustvo");
        }
        LovackoDrustvo drustvo = (LovackoDrustvo) entity;
        if (drustvo.getAdress().isEmpty() || drustvo.getCounty().isEmpty() || drustvo.getName().isEmpty()) {
            throw new ValidationException("Podaci nisu validni");
        }
        
    }
    
    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            drustvoRepository.add(entity, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lovacko drustvo ne moze da se sacuva");
        }
    }
    
    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) drustvoRepository.connect();
    }
    
    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        drustvoRepository.commit(conn);
    }
    
    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        drustvoRepository.rollback(conn);
    }
    
    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        drustvoRepository.disconnect(conn);
    }
    
}
