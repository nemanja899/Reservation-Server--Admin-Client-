/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import communication.Operation;
import domain.GeneralDObject;
import domain.Invoice;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.so.AbstractSO;
import project.so.ogranicenja.InvoiceOgranicenje;
import project.so.ogranicenja.OgranicenjaConstants;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class UpdateInvoiceSO extends AbstractSO {

    @Override
    protected void precondition(GeneralDObject entity, Object condition) throws ValidationException {
        Validator.startValidate().objectInstance(entity, Invoice.class, "Objekat nije racun")
                .throwIfInvalide();
        Invoice invoice = (Invoice) entity;

        try {
            Method m = OgranicenjaConstants.getMethod(Invoice.class.getSimpleName(), OgranicenjaConstants.UPDATE_OGRANICENJE);
            m.invoke(Class.forName(OgranicenjaConstants.CLASS_NAME(Invoice.class.getSimpleName())).newInstance(), invoice);

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UpdateInvoiceSO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getCause().getMessage());
        }
    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object condition, Connection conn) throws Exception {
        bbp.eddit(entity, condition, conn);
        return true;
    }

}
