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

/**
 *
 * @author User
 */
public class DeleteInvoiceSO extends AbstractSO{
     private final Repository invoiceStorrage;

    public DeleteInvoiceSO() {
        invoiceStorrage = new InvoiceRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (entity == null || !(entity instanceof Invoice)) {
            throw new ValidationException("Nije Racun");
        }
        Invoice invoice = (Invoice) entity;
        //provera vrednosnih ogranicenja
        if (invoice.isObradjen()) {
            throw new ValidationException("Racun je obradjen ne mozete ga obrisati");
        }
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            invoiceStorrage.delete((Invoice) entity, conn);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Zivotinja ne moze da se obrise");
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
