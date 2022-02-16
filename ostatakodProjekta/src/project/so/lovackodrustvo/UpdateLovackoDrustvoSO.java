/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.lovackodrustvo;

import domain.LovackoDrustvo;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.LovackoDrustvoRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class UpdateLovackoDrustvoSO extends AbstractSO {

    private final Repository drustvoRepository;

    public UpdateLovackoDrustvoSO() {
        drustvoRepository = new LovackoDrustvoRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(((LovackoDrustvoRepository) drustvoRepository).updateFirstObject(entity) instanceof LovackoDrustvo) || !(((LovackoDrustvoRepository) drustvoRepository).updateSecondPrimitive(entity) instanceof String)) {
            throw new ValidationException("Podaci nisu validni");
        }

    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            drustvoRepository.eddit(((LovackoDrustvoRepository) drustvoRepository).updateFirstObject(entity), ((LovackoDrustvoRepository) drustvoRepository).updateSecondPrimitive(entity), conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lovacko drustvo ne moze da se promeni");
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
