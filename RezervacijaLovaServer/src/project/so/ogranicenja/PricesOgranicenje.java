/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.Invoice;
import domain.InvoiceItem;
import domain.Prices;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.repository.connectionpool.DbConnectionPool;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;
import java.sql.Connection;

/**
 *
 * @author User
 */
public class PricesOgranicenje implements Ogranicenje {

    @Override
    public void AddPrecondition(GeneralDObject odk) throws ValidationException {
        Prices p = (Prices) odk;
        Validator.startValidate().validateNullObject(p.getSeason(), "Sezona mora biti uneta")
                .validateNullObject(p.getAnimal(), "Zivotinja mora biti izabrana")
                .validateNullObject(p.getDrustvo(), "Lovacko Drustvo mora biti uneto")
                .validateNullObject(p.getSeasonStart(), "Pocetak sezone mora biti izabrano")
                .validateNullObject(p.getSeasonEnd(), "Kraj sezone mora biti izabrano")
                .validateBigDecimal(p.getPrice(), "Cena mora biti veca od nule")
                .throwIfInvalide();
    }

    @Override
    public void UpdatePrecondition(GeneralDObject odk) throws ValidationException {
        Prices p = (Prices) odk;
        try {
            Connection conn = DbConnectionPool.getInstance().getConnection();
            InvoiceItem item = AbstractSO.bbp.searchInvoiceItemByPrices(p, conn);
            if (item != null) {
                throw new ValidationException("Racun sa cenom postoji ne moze se izmeniti!!");
            }
            Validator.startValidate().validateNullObject(p.getSeason(), "Sezona mora biti uneta")
                    .validateNullObject(p.getAnimal(), "Zivotinja mora biti izabrana")
                    .validateNullObject(p.getDrustvo(), "Lovacko Drustvo mora biti uneto")
                    .validateNullObject(p.getSeasonStart(), "Pocetak sezone mora biti izabrano")
                    .validateNullObject(p.getSeasonEnd(), "Kraj sezone mora biti izabrano")
                    .validateBigDecimal(p.getPrice(), "Cena mora biti veca od nule")
                    .throwIfInvalide();
        } catch (Exception ex) {
            Logger.getLogger(PricesOgranicenje.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getMessage());
        }
    }

    @Override
    public void DeletePrecondition(GeneralDObject odk) throws ValidationException {
        Prices p = (Prices) odk;
        Validator.startValidate().validateNullObject(p.getSeason(), "Sezona mora biti uneta")
                .validateNullObject(p.getAnimal(), "Zivotinja mora biti izabrana")
                .validateNullObject(p.getDrustvo(), "Lovacko Drustvo mora biti uneto")
                .throwIfInvalide();
    }

}
