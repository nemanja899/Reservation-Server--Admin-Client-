/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.GeneralDObject;
import domain.Invoice;
import domain.Prices;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.so.AbstractSO;
import project.so.AbstractSO;
import project.so.ogranicenja.OgranicenjaConstants;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class AddAllPricesSO extends AbstractSO{

    @Override
    protected void precondition(GeneralDObject entity, Object condition) throws ValidationException {
      
        List<Prices> priceses=(List<Prices>)condition;
        for(Prices p:priceses){
          try {
            Method m = OgranicenjaConstants.getMethod(Prices.class.getSimpleName(), OgranicenjaConstants.ADD_OGRANICENJE);
            m.invoke(Class.forName(OgranicenjaConstants.CLASS_NAME(Prices.class.getSimpleName())).newInstance(), p);
            
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UpdateInvoiceSO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getCause().getMessage());
        }
    }
                
    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object condition, Connection conn) throws Exception {
        bbp.AddAllPriceses((List<Prices>) condition,conn);
        return true;
    }
    
}
