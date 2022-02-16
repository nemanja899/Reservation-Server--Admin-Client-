/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Response implements Serializable{
    private Operation operation;
    private Object argument;
    private String poruka;
    private ResponseType responseType;

    
    public Response(Operation operation, Object argument, String poruka, ResponseType responseType) {
        this.operation = operation;
        this.argument = argument;
        this.poruka = poruka;
        this.responseType = responseType;
    }

   
    public Operation getOperation() {
        return operation;
    }

    public Object getArgument() {
        return argument;
    }

    public String getPoruka() {
        return poruka;
    }

    public ResponseType getResponseType() {
        return responseType;
    }
    
    
}
