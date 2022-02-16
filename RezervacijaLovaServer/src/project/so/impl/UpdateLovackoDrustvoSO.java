/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import domain.LovackoDrustvo;
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
public class UpdateLovackoDrustvoSO extends AbstractSO{

    @Override
    protected void precondition(GeneralDObject entity,Object obj) throws ValidationException {
         try {
            Validator.startValidate().objectInstance(entity, LovackoDrustvo.class, "Objekat nije Lovacko Drustvo")
                    .throwIfInvalide();
            LovackoDrustvo drustvo = (LovackoDrustvo) entity;
            Method m = OgranicenjaConstants.getMethod(LovackoDrustvo.class.getSimpleName(), OgranicenjaConstants.UPDATE_OGRANICENJE);
            m.invoke(Class.forName(OgranicenjaConstants.CLASS_NAME(LovackoDrustvo.class.getSimpleName())).newInstance(), drustvo);

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UpdateInvoiceSO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getCause().getMessage());
        }
    }

    @Override
    protected Object executeOperation(GeneralDObject entity,Object obj, Connection conn) throws Exception {
        
        bbp.eddit(entity, obj, conn);
        return true;
    }
    
    
}
