/*in
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.communication;

import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domain.Season;
import domain.User;
import java.net.Socket;

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

    public Response login(Request request) throws Exception {
        
        new Sender(socket).send(request);
        return (Response) new Receiver(socket).receive();
    }

    public void send(Request request) throws Exception {
        new Sender(socket).send(request);
    }

}
