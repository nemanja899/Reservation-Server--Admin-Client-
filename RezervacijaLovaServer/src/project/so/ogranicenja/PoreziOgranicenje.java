/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.Invoice;
import domain.InvoiceItem;
import domain.Porezi;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.repository.connectionpool.DbConnectionPool;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class PoreziOgranicenje implements Ogranicenje {

    @Override
    public void addPrecondition(GeneralDObject odk) throws ValidationException {
        Porezi porezi = (Porezi) odk;
        Validator.startValidate().validateBigDecimal(porezi.getPDV(), "Pdv mora biti veci od nule ")
                .validateBigDecimal(porezi.getProvision(), "Provizija mora biti veca od nule ")
                .validateSeasonString(porezi.getSeason())
                .throwIfInvalide();
    }

    @Override
    public void updatePrecondition(GeneralDObject odk) throws ValidationException {
        Porezi porezi = (Porezi) odk;
        try {
            Connection conn = DbConnectionPool.getInstance().getConnection();
            Invoice invoice = AbstractSO.bbp.searchInvoiceByPorezi(porezi, conn);
            if (invoice != null) {
                throw new ValidationException("Racun sa PDV-om postoji ne moze se izmeniti!!");
            }
            Validator.startValidate().validateBigDecimal(porezi.getPDV(), "Pdv mora biti veci od nule ")
                    .validateBigDecimal(porezi.getProvision(), "Provizija mora biti veca od nule ")
                    .validateSeasonString(porezi.getSeason())
                    .throwIfInvalide();
        } catch (Exception ex) {
            Logger.getLogger(PricesOgranicenje.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getMessage());
        }
    }

    @Override
    public void deletePrecondition(GeneralDObject odk) throws ValidationException {
        Porezi porezi = (Porezi) odk;
        Validator.startValidate().validateSeasonString(porezi.getSeason())
                .throwIfInvalide();
    }

}
