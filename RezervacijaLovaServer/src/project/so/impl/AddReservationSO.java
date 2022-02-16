/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.Animal;
import domain.GeneralDObject;
import domain.Reservation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.so.AbstractSO;
import static project.so.AbstractSO.bbp;
import project.so.ogranicenja.OgranicenjaConstants;

import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class AddReservationSO extends AbstractSO {

    @Override
    protected void precondition(GeneralDObject entity, Object condition) throws ValidationException {

        try {
            Validator.startValidate().objectInstance(entity, Reservation.class, "Objekat nije rezervacija")
                    .throwIfInvalide();
            Reservation reservation = (Reservation) entity;
            Method m = OgranicenjaConstants.getMethod(Reservation.class.getSimpleName(), OgranicenjaConstants.ADD_OGRANICENJE);
            m.invoke(Class.forName(OgranicenjaConstants.CLASS_NAME(Reservation.class.getSimpleName())).newInstance(), reservation);

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UpdateInvoiceSO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getCause().getMessage());
        }

    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object condition, Connection conn) throws Exception {
        bbp.add(entity, conn);
        return true;
    }

}
