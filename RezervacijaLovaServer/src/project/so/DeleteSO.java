/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so;

import domain.GeneralDObject;
import java.sql.Connection;

import validation.ValidationException;

/**
 *
 * @author User
 */
public class DeleteSO extends AbstractSO {

    @Override
    protected void precondition(GeneralDObject entity, Object obj) throws ValidationException {

    

    }

    @Override
    protected Object executeOperation(GeneralDObject entity,Object obj, Connection conn) throws Exception {
        bbp.delete(entity, conn);
        return true;
    }

}
