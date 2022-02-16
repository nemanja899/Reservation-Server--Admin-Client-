/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import domain.LovackoDrustvo;
import domain.Season;
import java.sql.Connection;
import project.so.AbstractSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class GetAllReservationByDrustvoBySeasonNoInvoiceSO extends AbstractSO {

    @Override
    protected void precondition(GeneralDObject entity, Object condition) throws ValidationException {
        Validator.startValidate().objectInstance(condition, Object[].class, "Objekat nije niz")
                .objectInstance(((Object[]) condition)[0], Season.class, "Prvi objekat nije sezona")
                .objectInstance(((Object[]) condition)[1], LovackoDrustvo.class, "Drugi objekat nije lovackoDrustvo")
                .throwIfInvalide();

    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object condition, Connection conn) throws Exception {
        return bbp.getAllReservationByDrustvoNoInvoice(condition, conn);
    }

}
