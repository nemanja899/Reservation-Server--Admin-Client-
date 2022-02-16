/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.impl;

import domain.Animal;
import domain.GeneralDObject;
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
public class DeleteAnimalSO extends AbstractSO {

    @Override
    protected void precondition(GeneralDObject entity, Object obj) throws ValidationException {
        try {
            Validator.startValidate().objectInstance(entity, Animal.class, "Objekat nije Divljac")
                    .throwIfInvalide();
            Animal animal = (Animal) entity;
            Method m = OgranicenjaConstants.getMethod(Animal.class.getSimpleName(), OgranicenjaConstants.DELETE_OGRANICENJE);
            m.invoke(Class.forName(OgranicenjaConstants.CLASS_NAME(Animal.class.getSimpleName())).newInstance(), animal);

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UpdateInvoiceSO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ValidationException(ex.getCause().getMessage());

        }
    }

    @Override
    protected Object executeOperation(GeneralDObject entity, Object obj, Connection conn) throws Exception {
        bbp.delete(entity, conn);
        return true;
    }

}
