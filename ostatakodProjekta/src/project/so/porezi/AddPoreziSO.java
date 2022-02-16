/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.porezi;

import domain.Porezi;
import java.sql.Connection;
import project.repository.db.impl.PoreziRepository;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class AddPoreziSO extends AbstractSO {

    private final PoreziRepository poreziStorrage;

    public AddPoreziSO() {
        poreziStorrage = new PoreziRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof Porezi)) {
            throw new ValidationException("Nisu dodatna placanja");
        }
        Porezi porezi = (Porezi) entity;
        String season = porezi.getSeason();
        Validator.startValidate()
               .validateSeasonString(season)
                .validateBigDecimal(porezi.getPDV(), "PDV ne moze biti manji od nule")
                .validateBigDecimal(porezi.getProvision(), "Provizija ne moze biti manja od nule")
                .throwIfInvalide();
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            poreziStorrage.add((Porezi) entity, conn);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Dodatna placanja ne mogu da se dodaju");
        }
    }

    @Override
    protected Connection startTransaction() throws Exception {
        return poreziStorrage.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        poreziStorrage.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        poreziStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        poreziStorrage.disconnect(conn);
    }

}
