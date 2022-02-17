/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import java.lang.reflect.Method;

/**
 *
 * @author User
 */
public class OgranicenjaConstants {
    public static final String PACKAGE="project.so.ogranicenja.";
    public static final String OGRANICENJE="Ogranicenje";
    public static final String ADD_OGRANICENJE="addPrecondition";
    public static final String UPDATE_OGRANICENJE="updatePrecondition";
    public static final String DELETE_OGRANICENJE="deletePrecondition";
    
    public static Method getMethod(String className,String CRUD) throws ClassNotFoundException, NoSuchMethodException{
        return Class.forName(CLASS_NAME(className)).getMethod(CRUD, GeneralDObject.class);
    }
    public static final String CLASS_NAME(String className){
        return PACKAGE+className+OGRANICENJE;
    }
}
