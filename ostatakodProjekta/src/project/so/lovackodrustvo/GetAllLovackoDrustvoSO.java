/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.lovackodrustvo;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.LovackoDrustvoRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllLovackoDrustvoSO extends AbstractSO {

    private final Repository drustvoRepository;

    public GetAllLovackoDrustvoSO() {
        drustvoRepository = new LovackoDrustvoRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        //nema objekat
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return drustvoRepository.getAll(conn);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lovacka drustva ne mogu da se ucitaju");
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
