/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controller;

import communication.Operation;
import communication.Request;
import communication.Response;
import communication.ResponseType;

import domain.Animal;
import domain.Invoice;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.communication.Communication;

/**
 *
 * @author User
 */
public class Controller {

    private static Controller instance;
    private User currentUser;
    private Map<Operation, Response> mapa;

    private Controller() {
        mapa = new HashMap<>();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Map<Operation, Response> getMapa() {
        return mapa;
    }

    public void putInMap(Operation operation, Response response) {
        mapa.put(operation, response);
    }

    public Object genericMethod(Request request) throws Exception {
        Communication.getInstance().send(request);
        while (!mapa.containsKey(request.getOperation())) {
        }
        Response response = mapa.get(request.getOperation());
        if (response.getResponseType().equals(ResponseType.SUCCESS)) {
            mapa.remove(request.getOperation());
            return response.getArgument();
        }
        throw new Exception(response.getPoruka());
    }

    public User login(User user) throws Exception {
       
        Request request = new Request(Operation.LOGIN, user,null);
        return (User) Communication.getInstance().login(request).getArgument();
    }

    public List<Season> getAllSeasons() throws Exception {

        return (List<Season>) genericMethod(new Request(Operation.GET_ALL_SEASON, new Season(), null));

    }

    public List<Animal> getAllAnimalsByAllowedNotInPrices(Season season) throws Exception {

        return (List<Animal>) genericMethod(new Request(Operation.GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON, new Animal(), new Object[]{season, currentUser.getLovackoDrustvoid()}));
    }

    public boolean addAllPrices(List<Prices> p) throws Exception {
        Request request = new Request(Operation.ADD_ALL_PRICES, null, null);
        request.setList(p);
        genericMethod(request);
        return true;
    }

    public List<Prices> getAllPricesesBySeasonByDrustvo(Season selectedSeason) throws Exception {
        List<Prices> priceses = new ArrayList<>();
        Request request = new Request(Operation.GET_ALL_PRICES_BY_SEASON, new Prices(), selectedSeason);
        List<Prices> priceses1 = (List<Prices>) genericMethod(request);
        for (Prices p : priceses1) {
            if (p.getDrustvo().equals(currentUser.getLovackoDrustvoid())) {
                priceses.add(p);
            }
        }
        return priceses;
    }

    public List<Animal> getAllAnimalsByAllowed() throws Exception {
        Request request = new Request(Operation.GET_ALL_ANIMALS, new Animal(), null);
        List<Animal> ans = (List<Animal>) genericMethod(request);
        List<Animal> anss = new ArrayList<>();
        ans.stream().forEach((c) -> {
            if (c.isAllowed()) {
                anss.add(c);
            }
        });
        return anss;
    }

    public void deletePrices(Prices p) throws Exception {
        Request request = new Request(Operation.DELETE_PRICES, p, null);
        genericMethod(request);
    }

    public void edditPrices(Prices p, Object[] object) throws Exception {
        Request request = new Request(Operation.UPDATE_PRICES, p, object);
        genericMethod(request);
    }

    public List<Reservation> getAllReservationsByDrustvoNoInvoice(Season season) throws Exception {
        System.out.println(season);
        Request request = new Request(Operation.GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE, new Reservation(), new Object[]{season, currentUser.getLovackoDrustvoid()});
        return (List<Reservation>) genericMethod(request);
    }

    public List<Invoice> getAllInvoicesByDrustvo() throws Exception {
        Request request = new Request(Operation.GET_ALL_INVOICE_BY_DRUSTVO, null, currentUser.getLovackoDrustvoid());
        return (List<Invoice>) genericMethod(request);
    }

    public Porezi searchPorezi(Season season) throws Exception {
        Request request = new Request(Operation.SEARCH_POREZ, new Porezi(), season.getSeason());
        return (Porezi) genericMethod(request);
    }

    public void addInvoice(Invoice invoice) throws Exception {
        System.out.println(invoice.getCurrency());
        Request request = new Request(Operation.ADD_INVOICE, invoice, null);
        genericMethod(request);
    }

    public void updateInvoice(Invoice invoice, Long id) throws Exception {
        Request request = new Request(Operation.UPDATE_INVOICE, invoice, id);
        genericMethod(request);
    }

}
