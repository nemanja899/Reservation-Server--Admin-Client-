/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.porezi;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.PoreziRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllPoreziSO extends AbstractSO{
     private final Repository poreziRepository;

    public GetAllPoreziSO() {
        poreziRepository = new PoreziRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        //nema objekat
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return poreziRepository.getAll(conn);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Porezi ne mogu da se ucitaju");
        }
        
    }

  @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) poreziRepository.connect();
    }
    
    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        poreziRepository.commit(conn);
    }
    
    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        poreziRepository.rollback(conn);
    }
    
    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        poreziRepository.disconnect(conn);
    }

}
