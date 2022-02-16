/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import domain.Reservation;
import java.sql.Connection;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetInvoiceItemsByReservationSO extends AbstractSO{

    @Override
    protected void precondition(GeneralDObject entity,Object con) throws ValidationException {
        //
    }

    @Override
    protected Object executeOperation(GeneralDObject entity,Object obj, Connection conn) throws Exception {
        return bbp.getInvoiceItemsByReservation((Reservation) entity, conn);
    }
    
}
