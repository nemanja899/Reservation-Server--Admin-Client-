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
public class GetAllPricesSO extends AbstractSO {

    private final Repository pricesRepository;

    public GetAllPricesSO() {
        pricesRepository = new PricesRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        // nema preduslova
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return pricesRepository.getAll(conn);
           
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cenovnici ne mogu biti ucitani");
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
