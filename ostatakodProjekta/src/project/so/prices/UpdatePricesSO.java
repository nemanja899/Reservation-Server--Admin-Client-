/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.prices;

import domain.Prices;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.PricesRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class UpdatePricesSO extends AbstractSO {

    private final Repository pricesRepository;

    public UpdatePricesSO() {
        pricesRepository = new PricesRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        Object[] obj = (Object[]) entity;
        Object[] pk= (Object[]) obj[1];
        if (!(obj[0] instanceof Prices) || !(pk[0] instanceof String)|| !(obj[1] instanceof Long) || !(obj[2] instanceof Long)) {
            throw new ValidationException("Podaci nisu validni");
        }

    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           pricesRepository.eddit(new PricesRepository().updateFirstObject(entity), new PricesRepository().updateSecondPrimitive(entity), conn);
           return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cenovnik nije izmenjen");
        }
    }

    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) pricesRepository.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        pricesRepository.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        pricesRepository.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        pricesRepository.disconnect(conn);
    }
}
