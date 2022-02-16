/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import domain.GeneralDObject;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author User
 */
public class Request implements Serializable {

    private Operation operation;
    private GeneralDObject argument;
    private String poruka;
    private Object condition;
    private Object listObjects; 

    public void setList(Object dObjects) {
        this.listObjects = dObjects;
    }

    public String getPoruka() {
        return poruka;
    }

    public Object getListObjects() {
        return listObjects;
    }

    public Request(Operation operation, GeneralDObject argument, Object condition) {
        this.operation = operation;
        if (argument != null) {
            this.argument = argument;
        }
        if (condition != null) {
            this.condition = condition;
        }
    }

    public Request() {
    }
    
    

    public Operation getOperation() {
        return operation;
    }

    public GeneralDObject getArgument() {
        return argument;
    }

    public Object getCondition() {
        return condition;
    }

}
