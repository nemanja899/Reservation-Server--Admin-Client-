/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.invoice;

import domain.Invoice;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.InvoiceRepository;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class UpdateInvoiceSO extends AbstractSO{
     private final Repository invoiceStorrage;
    
    public UpdateInvoiceSO() {
        invoiceStorrage = new InvoiceRepository();
    }
    
    @Override
    protected void precondition(Object entity) throws ValidationException {        
        Object[] obj = (Object[]) entity;
        if (!(obj[0] instanceof Invoice) || !(obj[1] instanceof Long)) {
            throw new ValidationException("Nije racun ili podaci nisu validni");
        }
        Invoice invoice = (Invoice) obj[0];
        Long id = (Long) obj[1];
        if (invoice.isObradjen()) {
            throw new ValidationException("Racun je obradjen ne moze da se izmeni");
        }
        Validator.startValidate().emptyList(invoice.getInvoiceItems(), "Nema stavki racuna")
            .validateBigDecimal(invoice.getTotalCost(), "Ukupna vrednost mora biti veca od nule")
                    .validateNullObject(invoice.getCreatedBy(), "Mora da sadrzi ko je kreirao racun")
                    .validateNullObject(invoice.getCurrency(), "Mora da sadrzi valutu")
                    .validateEmptyString(invoice.getNumber(), "Broj ne sme biti prazan")
                    .validateNullObject(invoice.getPorezi(), "Mora da sadrzi i dodatne troskove")
                    .validateNullObject(invoice.getReservation(), "Mora da sadrzi broj rezervacije")
                    .validateNullObject(invoice.getHunter(), "Mora da sadrzi podatke lovca")
                    .throwIfInvalide();
    }
    
    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           Invoice invoice= ((InvoiceRepository)invoiceStorrage).updateFirstObject(entity);
           Long id= ((InvoiceRepository)invoiceStorrage).updateSecondPrimitive(entity);
            invoiceStorrage.eddit(invoice, id, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Zivotinja nije izmenjena");
        }
    }
    
    @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) invoiceStorrage.connect();
    }
    
    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        invoiceStorrage.commit(conn);
    }
    
    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        invoiceStorrage.rollback(conn);
    }
    
    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        invoiceStorrage.disconnect(conn);
    }
}
