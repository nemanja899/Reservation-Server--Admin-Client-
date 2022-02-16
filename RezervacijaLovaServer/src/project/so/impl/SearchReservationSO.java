/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import java.sql.Connection;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class SearchReservationSO extends AbstractSO{

    @Override
    protected void precondition(GeneralDObject entity, Object condition) throws ValidationException {
        //
    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object condition, Connection conn) throws Exception {
        return bbp.search(entity, condition, conn);
    }
    
}
