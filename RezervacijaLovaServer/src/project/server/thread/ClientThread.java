/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.server.thread;

import com.mysql.cj.x.protobuf.MysqlxSession;
import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.ResponseType;
import communication.Sender;
import domain.Animal;
import domain.Hunter;
import domain.Invoice;
import domain.InvoiceItem;
import domain.LovackoDrustvo;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.controller.Controller;
import project.server.communication.Communication;

/**
 *
 * @author User
 */
public class ClientThread extends Thread {

    private Socket socket;
    private User user;
    private ServerThread server;

    public ClientThread(Socket socket,ServerThread server) {
        this.socket = socket;
        this.server=server;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Request request = (Request) new Receiver(socket).receive();

                Response response = handleClientRequest(request);
                if (response != null) {
                    new Sender(socket).send(response);

                }
            } catch (Exception ex) {
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Response handleClientRequest(Request request) {
        switch (request.getOperation()) {
            case LOGIN:
                return loginClient(request);
                
            case ADD_USER:
                return addUser(request);
                
            case ADMIN_LOGIN:
                return adminLogin(request);

            case ADD_ANIMAL:
                return addAnimal(request);

            case UPDATE_ANIMAL:
                return updateAnimal(request);

            case DELETE_ANIMAL:
                return deleteAnimal(request);

            case SEARCH_ANIMAL:
                return searchAnimal(request);

            case GET_ALL_ANIMALS:
                return getAllAnimals();

            case GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON:
                return getAllAnimalsByAllowed(request);

            case ADD_LOVACKO_DRUSTVO:

                return addDrustvo(request);

            case GET_ALL_LOVACKO_DRUSTVO:

                return getAllDrustva();

            case UPDATE_LOVACKO_DRUSTVO:

                return updateDrustvo(request);

            case DELETE_LOVACKO_DRUSTVO:

                return deleteDrustvo(request);

            case GET_ALL_DRUSTVO_NOPRICES:

                return getAllDrustvoWithoutPrices(request);

            case GET_ALL_PRICES:

                return getAllPrices();

            case GET_ALL_PRICES_BY_SEASON:

                return getAllPricesBySeason(request);

            case SEARCH_PRICES:

                return searchPrices(request);

            case DELETE_PRICES:

                return deletePrices(request);

            case ADD_ALL_PRICES:

                return addAllPriceses(request);
            case UPDATE_PRICES:
                return updatePrices(request);
            case UPDATE_HUNTER:

                return updateHunter(request);

            case SEARCH_HUNTER:

                return searchHunter(request);

            case DELETE_HUNTER:

                return deleteHunter(request);
            case GET_ALL_HUNTER:

                return getAllHunters();

            case RESERVATE:

                return reservate(request);

            case GET_ALL_RESERVATION:

                return getAllReservations();

            case GET_ALL_RESERVATION_BY_SEASON:

                return getAllReservationBySeason(request);

            case GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE:
                return getAllReservationBySeasonByDrustvoNoInvoice(request);

            case DELETE_RESERVATION:

                return deletereservation(request);

            case GET_ALL_SEASON:

                return getAllSeasons();

            case ADD_POREZ:

                return addPorez(request);

            case UPDATE_POREZ:

                return updatePorez(request);

            case SEARCH_POREZ:

                return searchPorez(request);

            case DELETE_POREZ:

                return deletePorez(request);

            case GET_ALL_PROFITS_PER_SEASON:

                return getAllProfitsSeason();

            case GET_HUNTED_ANIMALS_NUM:

                return getHuntedAnimals();

            case GET_INVOICE_ITEMS_BY_RESERVATION:

                return getInvoiceItemsByReservation(request);

            case GET_ALL_INVOICE_BY_DRUSTVO:
                return getAllInvoiceByDrustvo(request);
                
            case ADD_INVOICE:
                return addInvoice(request);

            case UPDATE_INVOICE:
            return updateInvoice(request);

            case NOTIFY:
                return sendNotifications(request);

            case EXIT:
                server.getClients().remove(this);
                Communication.getInstance().removeClient(this);
                
                return null;
            default:
                return null;
        }
    }

    public Response updateInvoice(Request request) {
        try {
            Controller.getInstance().updateInvoice(request.getArgument(), request.getCondition());
            return new Response(Operation.UPDATE_INVOICE, null, "Racun je sacuvan", ResponseType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Operation.UPDATE_INVOICE, null, e.getMessage(), ResponseType.ERROR);
        }
    }

    public Response addUser(Request request) {
        try {
            User user = (User) request.getArgument();
            Controller.getInstance().addUser(user);
            return new Response(Operation.ADD_USER, user, "Korisnik je dodat", ResponseType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Operation.ADD_USER, null, e.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllReservationBySeason(Request request) {
        try {
            Object condition = request.getCondition();
            List<Reservation> reservations = Controller.getInstance().getAllReservationsBySeason((Season) condition);
            return new Response(Operation.GET_ALL_RESERVATION_BY_SEASON, reservations, "Rezervacije su ucitane", ResponseType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Operation.GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE, null, e.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllReservationBySeasonByDrustvoNoInvoice(Request request) {
        try {
            Object condition = request.getCondition();
            List<Reservation> reservations = Controller.getInstance().getAllReservationBySeasonByDrustvoNoInvoice(condition);
            return new Response(Operation.GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE, reservations, "Rezervacije su poslate", ResponseType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Operation.GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE, null, e.getMessage(), ResponseType.ERROR);

        }
    }

    public Response addInvoice(Request request) {
        try {

            Invoice invoice = (Invoice) request.getArgument();
            Controller.getInstance().addInvoice(invoice);
            return new Response(Operation.ADD_INVOICE, null, "Racun je sacuvan", ResponseType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Operation.ADD_INVOICE, null, e.getMessage(), ResponseType.ERROR);
        }
    }

    public Response sendNotifications(Request request) {
        try {
            if (request.getArgument() instanceof Reservation) {

                Communication.getInstance().sendDeleteReservation((Reservation) request.getArgument());
                return new Response(Operation.NOTIFY, null, "Notifikacija je poslata", ResponseType.SUCCESS);

            } else if (request.getArgument() instanceof Animal) {
                Communication.getInstance().sendAnimalNotAllowedNotification((Animal) request.getArgument());
                return new Response(Operation.NOTIFY, null, "Notifikacija je poslata", ResponseType.SUCCESS);
            } else if (request.getArgument() instanceof LovackoDrustvo) {

                Communication.getInstance().notifyDrustvaPrices((LovackoDrustvo) request.getArgument(), (Season) request.getCondition());
                return new Response(Operation.NOTIFY, null, "Lovacko drustvo je obavesteno", ResponseType.SUCCESS);
            } else {
                Communication.getInstance().notifyAllDrustvaPrices((List<LovackoDrustvo>) request.getListObjects(), (Season) request.getCondition());
                return new Response(Operation.NOTIFY, null, "Lovacka drustva su obavestena", ResponseType.SUCCESS);

            }

        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.NOTIFY, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response getInvoiceItemsByReservation(Request request) {
        Reservation reservation = (Reservation) request.getArgument();

        try {
            List<InvoiceItem> items = Controller.getInstance().getInvoiceItemsByReservation(reservation);
            return new Response(Operation.GET_INVOICE_ITEMS_BY_RESERVATION, items, "Stavke racuna su ucitane", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_INVOICE_ITEMS_BY_RESERVATION, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response getHuntedAnimals() {
        try {
            List<InvoiceItem> items = Controller.getInstance().getNumberOfHuntedAnimals();
            return new Response(Operation.GET_HUNTED_ANIMALS_NUM, items, "Stavke ulovljenih zivotinja su ucitane", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_HUNTED_ANIMALS_NUM, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllProfitsSeason() {
        try {
            List<Object> profits = Controller.getInstance().getAllProfitsPerSeason();
            return new Response(Operation.GET_ALL_PROFITS_PER_SEASON, profits, "Profiti su ucitani", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_PROFITS_PER_SEASON, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response deletePorez(Request request) {
        Porezi porezi = (Porezi) request.getArgument();

        try {
            Controller.getInstance().deletePorezi(porezi);
            return new Response(Operation.DELETE_POREZ, null, "Porez je obrisan", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.DELETE_POREZ, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response searchPorez(Request request) {
        try {
            Porezi porezi = Controller.getInstance().searchPorezi((String) request.getCondition());
            return new Response(Operation.SEARCH_POREZ, porezi, "Porez je nadjen", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.SEARCH_POREZ, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response updatePorez(Request request) {
        Porezi porezi = (Porezi) request.getArgument();

        try {
            Controller.getInstance().updatePorezi(porezi, (Season) request.getCondition());
            return new Response(Operation.UPDATE_POREZ, null, "Porez je uspesno dodat", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.UPDATE_POREZ, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response addPorez(Request request) {
        Porezi porez = (Porezi) request.getArgument();

        try {
            Controller.getInstance().addPorezi(porez);
            return new Response(Operation.ADD_POREZ, null, "Porez je dodat", ResponseType.SUCCESS);

        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.ADD_POREZ, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response getAllSeasons() {
        try {
            List<Season> seasons = Controller.getInstance().getAllSeasons();
            return new Response(Operation.GET_ALL_SEASON, seasons, "Sezone su ucitane", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_SEASON, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response deletereservation(Request request) {
        Reservation reservation = (Reservation) request.getArgument();

        try {
            Controller.getInstance().deleteReservation(reservation);
            return new Response(Operation.DELETE_RESERVATION, null, "Rezervacija je ponistena", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.DELETE_RESERVATION, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllReservations() {
        try {
            List<Reservation> reservations = Controller.getInstance().getAllReservations();
            return new Response(Operation.GET_ALL_RESERVATION, reservations, "Rezervacije su ucitane", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_RESERVATION, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response reservate(Request request) {
        Reservation reservation = (Reservation) request.getArgument();

        try {
            Controller.getInstance().reservate(reservation);
            Communication.getInstance().sendReservation(reservation);
            return new Response(Operation.RESERVATE, null, "Rezervacija je uspesna", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.RESERVATE, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllHunters() {
        try {
            List<Hunter> hunters = Controller.getInstance().getAllHunter();
            return new Response(Operation.GET_ALL_HUNTER, hunters, "Lovci su ucitani", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_HUNTER, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response deleteHunter(Request request) {
        Hunter hunter = (Hunter) request.getArgument();
        {
            try {
                Controller.getInstance().deleteHunter(hunter);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Operation.DELETE_HUNTER, null, ex.getMessage(), ResponseType.ERROR);

            }
        }
        return new Response(Operation.DELETE_HUNTER, null, "Lovac je obrisan", ResponseType.SUCCESS);
    }

    public Response searchHunter(Request request) {
        Hunter hunter;
        try {
            hunter = Controller.getInstance().searchHuNter((String) request.getCondition());
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.SEARCH_HUNTER, null, ex.getMessage(), ResponseType.ERROR);

        }
        return new Response(Operation.SEARCH_HUNTER, hunter, "Lovac je pronadjen", ResponseType.SUCCESS);
    }

    public Response updateHunter(Request request) {
        Hunter hunter = (Hunter) request.getArgument();
        String oldPk = (String) request.getCondition();
        {
            try {
                Controller.getInstance().updateHunter(hunter, oldPk);
                return new Response(Operation.UPDATE_HUNTER, null, "Lovac je promenjen", ResponseType.SUCCESS);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Operation.UPDATE_HUNTER, null, ex.getMessage(), ResponseType.ERROR);

            }
        }
    }

    public Response searchPrices(Request request) {
        try {
            Prices prices = Controller.getInstance().searchPrices(request.getCondition());
            return new Response(Operation.SEARCH_PRICES, prices, "Cenovnik je nadjen", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.SEARCH_PRICES, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllPricesBySeason(Request request) {
        try {
            List<Prices> priceses = Controller.getInstance().getAllPricesBySeason((Season) request.getCondition());
            return new Response(Operation.GET_ALL_PRICES_BY_SEASON, priceses, "Uspesno ucitan cenovnik", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_PRICES_BY_SEASON, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response getAllPrices() {
        try {

            List<Prices> priceses = Controller.getInstance().getAllPriceses();
            return new Response(Operation.GET_ALL_PRICES, priceses, "Cenovnici su ucitani", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_PRICES, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllDrustvoWithoutPrices(Request request) {

        try {
            List<LovackoDrustvo> drustva = Controller.getInstance().getAllDrustvoWithoutPrices((Season) request.getCondition());
            return new Response(Operation.GET_ALL_DRUSTVO_NOPRICES, drustva, "Drustva su ucitana", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_DRUSTVO_NOPRICES, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response adminLogin(Request request) {
        try {
            Communication.getInstance().adminLogin((User) request.getArgument());
            user = (User) request.getArgument();
            return new Response(Operation.ADMIN_LOGIN, user, "Uspesno logovanje", ResponseType.SUCCESS);

        } catch (Exception ex) {
            return new Response(Operation.ADMIN_LOGIN, "", ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response deleteDrustvo(Request request) {
        LovackoDrustvo lovackoDrustvo = (LovackoDrustvo) request.getArgument();

        try {
            Controller.getInstance().deleteLovackoDrustvo(lovackoDrustvo);
            return new Response(Operation.DELETE_LOVACKO_DRUSTVO, null, "Uspesno obrisano drustvo", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.DELETE_LOVACKO_DRUSTVO, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response updateDrustvo(Request request) {
        try {
            LovackoDrustvo drustvo = (LovackoDrustvo) request.getArgument();
            Controller.getInstance().updateLovackoDrustvo(drustvo, (String) request.getCondition());
            return new Response(Operation.UPDATE_LOVACKO_DRUSTVO, null, "Uspesno promenjeno drustvo", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.UPDATE_LOVACKO_DRUSTVO, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response addDrustvo(Request request) {
        try {
            LovackoDrustvo drustvo = (LovackoDrustvo) request.getArgument();
            Controller.getInstance().addLovackoDrustvo(drustvo);
            return new Response(Operation.ADD_LOVACKO_DRUSTVO, null, "Lovacko Drustvo je dodato", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.ADD_LOVACKO_DRUSTVO, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response getAllDrustva() {
        try {
            List<LovackoDrustvo> drustva = Controller.getInstance().getAllLovackoDrustvo();
            return new Response(Operation.GET_ALL_LOVACKO_DRUSTVO, drustva, "Lovacka drustva su ucitana", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_LOVACKO_DRUSTVO, null, ex.getMessage(), ResponseType.ERROR);

        }
    }

    public Response getAllAnimals() {
        try {
            List<Animal> animals = Controller.getInstance().getAllAnimals();
            return new Response(Operation.GET_ALL_ANIMALS, animals, "Divljaci su ucitane", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_ANIMALS, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response searchAnimal(Request request) {
        Long pk = (Long) request.getCondition();

        try {
            Animal animal = Controller.getInstance().searchAnimal(pk);
            return new Response(Operation.SEARCH_ANIMAL, animal, "Zivotinja je nadjena", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.SEARCH_ANIMAL, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Response deleteAnimal(Request request) {
        Animal animal = (Animal) request.getArgument();
        {
            try {
                Controller.getInstance().deleteAnimal(animal);
                return new Response(Operation.DELETE_ANIMAL, null, "Uspesno obrisana divljac", ResponseType.SUCCESS);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Operation.DELETE_ANIMAL, null, ex.getMessage(), ResponseType.ERROR);
            }
        }
    }

    public Response updateAnimal(Request request) {
        Animal animal = (Animal) request.getArgument();
        Long oldPk = (Long) request.getCondition();
        {
            try {
                Controller.getInstance().updateAnimal(animal, oldPk);
                return new Response(Operation.UPDATE_ANIMAL, null, "Divljac je uspesno sacuvana", ResponseType.SUCCESS);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Operation.UPDATE_ANIMAL, null, ex.getMessage(), ResponseType.ERROR);
            }
        }
    }

    public Response addAnimal(Request request) {
        Animal animal = (Animal) request.getArgument();
        {
            try {
                Controller.getInstance().addAnimal(animal);
                return new Response(Operation.ADD_ANIMAL, null, "Uspesno dodata divljac", ResponseType.SUCCESS);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Operation.ADD_ANIMAL, null, ex.getMessage(), ResponseType.ERROR);

            }
        }
    }

    public Response loginClient(Request request) {
        User u = (User) request.getArgument();

        {
            try {
                user = Controller.getInstance().login(u);

                Response response = new Response(Operation.LOGIN, user, "Dobro dosli: " + user, ResponseType.SUCCESS);
                new Sender(socket).send(response);
                Communication.getInstance().addOnlineClient(this);
                if (Communication.getInstance().checkBacklog(this)) {
                    Communication.getInstance().sendAllBackLogs(this);
                }
                return null;
            } catch (Exception ex) {
                return new Response(Operation.LOGIN, null, ex.getMessage(), ResponseType.ERROR);
            }
        }
    }

    private Response getAllAnimalsByAllowed(Request request) {
        try {
            List<Animal> animals = Controller.getInstance().GetAllAnimalByConditionAllowedBySeasonByDrustvo(request.getArgument(), request.getCondition());
            return new Response(Operation.GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON, animals, "Lista divljaci je ucitana", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    private Response addAllPriceses(Request request) {
        try {
            Controller.getInstance().addAllPrices((List<Prices>) request.getListObjects());
            return new Response(Operation.ADD_ALL_PRICES, null, "Lista cenovnika je dodata", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.ADD_ALL_PRICES, null, ex.getMessage(), ResponseType.ERROR);
        }

    }

    private Response deletePrices(Request request) {
        try {
            Controller.getInstance().deletePrices((Prices) request.getArgument());
            return new Response(Operation.DELETE_PRICES, null, "Stavka cenovnika je obrisana", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.DELETE_PRICES, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    private Response updatePrices(Request request) {
        try {
            Controller.getInstance().updatePrices((Prices) request.getArgument(), request.getCondition());
            return new Response(Operation.UPDATE_PRICES, null, "Stavka cenovnika je izmenjena", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.UPDATE_PRICES, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    private Response getAllInvoiceByDrustvo(Request request) {
        try {
            List<Invoice> invoices = Controller.getInstance().getAllInvoiceByDrustvo(new Invoice(), request.getCondition());
            return new Response(Operation.GET_ALL_INVOICE_BY_DRUSTVO, invoices, "Liste racuna je ucitana", ResponseType.SUCCESS);
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Operation.GET_ALL_INVOICE_BY_DRUSTVO, null, ex.getMessage(), ResponseType.ERROR);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

}
