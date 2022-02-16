/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.communication;

import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Communication {

    private static Communication instance;
    
    private Socket socket;
    
    private Communication() {
    }
    
    public static Communication getInstance() {
        if (instance == null) {
            instance = new Communication();
        }
        return instance;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Response sendAndReceive(Request request) throws Exception {
        try {
            new Sender(socket).send(request);
            return (Response) new Receiver(socket).receive();
        } catch (Exception ex) {
            throw new Exception("Greska u slanju i primanju zahteva");
        }
        
    }
}
