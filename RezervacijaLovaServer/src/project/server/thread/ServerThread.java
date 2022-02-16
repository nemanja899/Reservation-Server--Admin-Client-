/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.server.thread;

import communication.Operation;
import communication.Response;
import communication.ResponseType;
import communication.Sender;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ServerThread extends Thread {
    
    private List<ClientThread> clients;
    private ServerSocket serverSocket;
    
    public ServerThread(ServerSocket socket) {
        this.serverSocket = socket;
        clients = new ArrayList<>();
    }
    
    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Klijent se povezao");
                ClientThread client = new ClientThread(socket,this);
                
                client.start();
                clients.add(client);
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<ClientThread> getClients() {
        return clients;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public void zatvoriSveKlijente() {
        clients.stream().forEach((c) -> {
            try {
               
                if(c.getUser().getLovackoDrustvoid()!=null){
                    Response r= new Response(Operation.CLOSE, null, "Odjava sa servera", ResponseType.SUCCESS);
                    new Sender(c.getSocket()).send(r);
                }
                 c.getSocket().close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
}
