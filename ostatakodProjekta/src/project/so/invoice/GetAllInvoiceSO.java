/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.invoice;

import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.InvoiceRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllInvoiceSO extends AbstractSO{
      private final Repository invoiceStorrage;

    public GetAllInvoiceSO() {
        invoiceStorrage = new InvoiceRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        //nema ogranicenja ni vrednosnih ni strukturnih
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
           return invoiceStorrage.getAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Racuni ne mogu da se ucitaju");
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
