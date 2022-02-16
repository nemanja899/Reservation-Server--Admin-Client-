/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import java.sql.Connection;
import project.so.AbstractSO;
import static project.so.AbstractSO.bbp;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllAnimalSO extends AbstractSO{

    @Override
    protected void precondition(GeneralDObject entity,Object con) throws ValidationException {
        //nema preduslova
    }

    @Override
    protected Object executeOperation(GeneralDObject entity,Object con, Connection conn) throws Exception {
       return bbp.getAll(entity, conn);
    }
    
}
