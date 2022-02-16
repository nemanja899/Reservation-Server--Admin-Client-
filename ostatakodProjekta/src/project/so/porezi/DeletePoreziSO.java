/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.porezi;


import domain.Porezi;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.PoreziRepository;

import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class DeletePoreziSO extends AbstractSO {
     private final Repository poreziRepository;

    public DeletePoreziSO() {
        poreziRepository = new PoreziRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (entity == null || !(entity instanceof Porezi)) {
            throw new ValidationException("Nisu Porezi");
        }
        Porezi porezi = (Porezi) entity;
        if (porezi.getSeason().isEmpty()) {
            throw new ValidationException("Podaci nisu validni");
        }
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            poreziRepository.delete(entity, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Porez za sezonu ne moze da se obrise");
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
