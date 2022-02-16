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
import validation.Validator;

/**
 *
 * @author User
 */
public class UpdatePoreziSO extends AbstractSO {
    
    private final Repository poreziRepository;
    
    public UpdatePoreziSO() {
        poreziRepository = new PoreziRepository();
    }
    
    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(((PoreziRepository) poreziRepository).updateFirstObject(entity) instanceof Porezi) || !(((PoreziRepository) poreziRepository).updateSecondPrimitive(entity) instanceof String)) {
            throw new ValidationException("Podaci nisu validni");
        }
        Porezi porezi=((PoreziRepository) poreziRepository).updateFirstObject(entity);
        String season = porezi.getSeason();
        Validator.startValidate()
                .validateBigDecimal(porezi.getPDV(), "Pdv nije validan broj")
                .validateBigDecimal(porezi.getProvision(), "Provizija nije validan broj")
                .validateSeasonString(season)
                .throwIfInvalide();
    }
    
    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            poreziRepository.eddit(((PoreziRepository) poreziRepository).updateFirstObject(entity), ((PoreziRepository) poreziRepository).updateSecondPrimitive(entity), conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Porez ne moze da se promeni");
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
