/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.server.communication;

import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.ResponseType;
import communication.Sender;
import domain.Animal;
import domain.LovackoDrustvo;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.controller.Controller;
import project.server.thread.ClientThread;

/**
 *
 * @author User
 */
public class Communication {

    private static Communication instance;

    private Map<LovackoDrustvo, ClientThread> onLineClients;
    private User admin;
    private Map<LovackoDrustvo, BackLog> mapDrustvoBacklog;

    private Communication() {
        onLineClients = new HashMap<>();
        mapDrustvoBacklog = new HashMap<>();
        admin = new User(null, null, null, "admin@savez.com", "admin99");
    }

    public boolean adminLogin(User u) throws Exception {
        if (admin.equals(u)) {
            if (u.getPassword().equals(admin.getPassword())) {
                return true;
            } else {
                throw new Exception("Pogresna sifra");
            }
        }
        throw new Exception("Pogresan email");
    }

    public Map<LovackoDrustvo, ClientThread> getOnLineClients() {
        return onLineClients;
    }

    public static Communication getInstance() {
        if (instance == null) {
            instance = new Communication();
        }
        return instance;
    }

    public void removeClient(ClientThread clientThread) {
        onLineClients.remove(clientThread.getUser().getLovackoDrustvoid());
    }

    public void addOnlineClient(ClientThread c) {
        onLineClients.put(c.getUser().getLovackoDrustvoid(), c);
    }
// obavestavamo sva lovacka drustva da naprave cenovnik za sezonu koja nisu

    public void notifyAllDrustvaPrices(List<LovackoDrustvo> drustva, Season season) throws Exception {
        for (LovackoDrustvo drustvo : drustva) {
            if (onLineClients.containsKey(drustvo)) {
                try {
                    Response response = new Response(Operation.NOTIFY, drustvo, "Molimo vas kreirajte cenovnik" + season + "!!", ResponseType.SUCCESS);
                    new Sender(onLineClients.get(drustvo).getSocket()).send(response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception("Doslo je do greske u slanju poruke");
                }
            } else {
                if (mapDrustvoBacklog.containsKey(drustvo)) {
                    mapDrustvoBacklog.get(drustvo).getBacklogs().add(drustvo);
                } else {
                    BackLog bl = new BackLog();
                    bl.getBacklogs().add(drustvo);
                    mapDrustvoBacklog.put(drustvo, bl);
                }
            }

        }
    }
// obavestavamo da je rezervisano

    public void sendReservation(Reservation reservation) throws Exception {
        Response response = new Response(Operation.NOTIFY, reservation, "Stigla je rezervacija!", ResponseType.SUCCESS);
        sendOrBackLog(reservation.getDrustvo(),"Greska u obavestenju rezervacije!", response);
    }
// obavestavamo da li je zabranjen lov za zivotinju

    public void sendAnimalNotAllowedNotification(Animal a) throws Exception {
        List<LovackoDrustvo> drustva = Controller.getInstance().getAllLovackoDrustvo();
        Response response = new Response(Operation.NOTIFY, a, "Zivotinja " + a.getShortName() + "je zabranjena za lov!!", ResponseType.SUCCESS);
        for (LovackoDrustvo drustvo : drustva) {
            sendOrBackLog(drustvo, "Greska u obavestenju o zabrani lova", response);
        }
    }

// obavestavamo samo jedno lovacko drustvo da napravi cenovnik
    public void notifyDrustvaPrices(LovackoDrustvo drustvo, Season selectedSeason) throws Exception {
        Response response = new Response(Operation.NOTIFY, drustvo, "Kreirajte Cenovnik za " + selectedSeason + "!!", ResponseType.SUCCESS);
        sendOrBackLog(drustvo,"Greska u obavestavanju lovackog drustva", response);

    }
// obavestavamo da je rezervacija otkazana

    public void sendDeleteReservation(Reservation reservation) throws Exception {
        Response response = new Response(Operation.NOTIFY, reservation, "Rezervacija je otkazana!", ResponseType.ERROR);
        sendOrBackLog(reservation.getDrustvo(), "Greska u obavestenju otkaza rezervacije", response);
    }
// saljemo sve beklogove kada se korisnik konektuje

    public void sendAllBackLogs(ClientThread client) throws Exception {

        for (Object obj : mapDrustvoBacklog.get(client.getUser().getLovackoDrustvoid()).getBacklogs()) {
            if (obj instanceof Response) {
                try {
                    new Sender(client.getSocket()).send(obj);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception("Greska u slanju backloga");
                }
            } else {
                if (obj instanceof LovackoDrustvo) {
                    Response response = new Response(Operation.NOTIFY, obj, "Molimo vas kreirajte cenovnik!!", ResponseType.SUCCESS);
                    new Sender(client.getSocket()).send(response);
                } else {
                    throw new Exception("Pogresan objekat");
                }
            }
            //  mapDrustvoBacklog.get(client.getUser().getLovackoDrustvoid()).getBacklogs().remove(obj);
        }

        mapDrustvoBacklog.remove(client.getUser().getLovackoDrustvoid());

    }

    public void sendOrBackLog(LovackoDrustvo drustvo, String greska, Response response) throws Exception {
        if (onLineClients.containsKey(drustvo)) {
            try {
                new Sender(onLineClients.get(drustvo).getSocket()).send(response);
            } catch (Exception e) {
                Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, e);
                throw new Exception(greska);
            }
        } else {
            if (mapDrustvoBacklog.containsKey(drustvo)) {   // dodajemo objekat u backlog listu 
                mapDrustvoBacklog.get(drustvo).getBacklogs().add(response);
            } else {
                BackLog bl = new BackLog();
                bl.getBacklogs().add(response);        // da li je prvi backlog
                mapDrustvoBacklog.put(drustvo, bl);
            }
        }
    }
// proveravamo becklog da li u mapi drustvo backlog postoji drustvo kao kljuc

    public boolean checkBacklog(ClientThread client) {
        if (mapDrustvoBacklog.containsKey(client.getUser().getLovackoDrustvoid())) {
            return true;
        }
        return false;
    }
}
