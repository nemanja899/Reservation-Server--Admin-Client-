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
import communication.Sender;
import domain.Animal;
import domain.Hunter;
import domain.InvoiceItem;
import domain.LovackoDrustvo;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import project.communication.Communication;

/**
 *
 * @author User
 */
public class Controller {

    private static Controller instance;
    private User currentUser;
    private Controller() {

    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public Response getResponse(Request request) throws Exception {
        Response response = Communication.getInstance().sendAndReceive(request);
        sendError(response);
        return response;
    }

    public void sendError(Response response) throws Exception {
        if (response.getResponseType().equals(ResponseType.ERROR)) {
            throw new Exception(response.getPoruka());
        }
    }

    public User login(User user) throws Exception {
       
        Request request= new Request(Operation.ADMIN_LOGIN, user,null);
        return (User) getResponse(request).getArgument();
    }
    
      public void addUser(User user) throws Exception {
          Request request=new Request(Operation.ADD_USER, user, null);
          getResponse(request);
    }


    public void addLovackoDrustvo(LovackoDrustvo drustvo) throws Exception {
        Request request = new Request(Operation.ADD_LOVACKO_DRUSTVO, drustvo,null);
        getResponse(request);
    }

    public LovackoDrustvo searchLovackoDrustvo(String name) throws Exception {
        Request request = new Request(Operation.SEARCH_LOVACKO_DRUSTVO, null,name);

        return (LovackoDrustvo) getResponse(request).getArgument();
    }

    public void updateLovackoDrustvo(LovackoDrustvo drustvo, String name) throws Exception {
        Request request = new Request(Operation.UPDATE_LOVACKO_DRUSTVO, drustvo, name);
        getResponse(request);

    }

    public void deleteLovackoDrustvo(LovackoDrustvo drustvo) throws Exception {
        Request request = new Request(Operation.DELETE_LOVACKO_DRUSTVO, drustvo,null);
        getResponse(request);
    }

    public List<LovackoDrustvo> getAllLovackoDrustvo() throws Exception {
        Request request = new Request(Operation.GET_ALL_LOVACKO_DRUSTVO, null,null);
        return (List<LovackoDrustvo>) getResponse(request).getArgument();
    }

    public List<LovackoDrustvo> getAllDrustvoWithoutPrices(Season season) throws Exception {
        Request request = new Request(Operation.GET_ALL_DRUSTVO_NOPRICES,null, season);

        return (List<LovackoDrustvo>) getResponse(request).getArgument();
    }

    public void addAnimal(Animal animal) throws Exception {
        Request request = new Request(Operation.ADD_ANIMAL, animal,null);
        getResponse(request);
    }

    public void deleteAnimal(Animal animal) throws Exception {
        Request request = new Request(Operation.DELETE_ANIMAL, animal,null);
        getResponse(request);
    }

    public void updateAnimal(Animal animal, Long oldPk) throws Exception {
        Request request = new Request(Operation.UPDATE_ANIMAL,animal, oldPk);
        getResponse(request);
    }

    public List<Animal> getAllAnimals() throws Exception {
        Request request = new Request(Operation.GET_ALL_ANIMALS, null,null);
        return (List<Animal>) getResponse(request).getArgument();
    }

    public Animal searchAnimal(Long id) throws Exception {
        Request request = new Request(Operation.SEARCH_ANIMAL,null, id);
        return (Animal) getResponse(request).getArgument();
    }

    public List<Prices> getAllPriceses() throws Exception {
        Request request = new Request(Operation.GET_ALL_PRICES, null,null);
        return (List<Prices>) getResponse(request).getArgument();
    }

    public List<Prices> getAllPricesBySeason(Season selectedSeason) throws Exception {
        Request request = new Request(Operation.GET_ALL_PRICES_BY_SEASON,null, selectedSeason);
        return (List<Prices>) getResponse(request).getArgument();
    }


    public void updateHunter(Hunter hunter, String passport) throws Exception {
        Request request = new Request(Operation.UPDATE_HUNTER,hunter, passport);
        getResponse(request);
    }

    public Hunter searchHuNter(String passport) throws Exception {
        Request request = new Request(Operation.SEARCH_HUNTER,null, passport);
        return (Hunter) getResponse(request).getArgument();

    }

    public void deleteHunter(Hunter hunter) throws Exception {
        Request request = new Request(Operation.DELETE_HUNTER, hunter,null);
        getResponse(request);
    }

    public List<Hunter> getAllHunter() throws Exception {
        Request request = new Request(Operation.GET_ALL_HUNTER, null,null);
        return (List<Hunter>) getResponse(request).getArgument();
    }

    public void reservate(Reservation reservation) throws Exception {
        Request request = new Request(Operation.RESERVATE, reservation,null);
        getResponse(request);
    }

    public List<Reservation> getAllReservations() throws Exception {
        Request request = new Request(Operation.GET_ALL_RESERVATION, null,null);
        return (List<Reservation>) getResponse(request).getArgument();
    }

    public List<Reservation> getAllReservationsBySeason(Season season) throws Exception {
        Request request = new Request(Operation.GET_ALL_RESERVATION_BY_SEASON,null, season);
        return (List<Reservation>) getResponse(request).getArgument();
    }

    public void deleteReservation(Reservation reservation) throws Exception {
        Request request = new Request(Operation.DELETE_RESERVATION, reservation,null);
        getResponse(request);
    }

    public List<Season> getAllSeasons() throws Exception {
        Request request = new Request(Operation.GET_ALL_SEASON, null,null);
        return (List<Season>) getResponse(request).getArgument();
    }

    public void addPorezi(Porezi porez) throws Exception {
        Request request = new Request(Operation.ADD_POREZ,porez, null);
        getResponse(request);
    }

    public void updatePorezi(Porezi porezi, Season selectedSeason) throws Exception {
        Request request = new Request(Operation.UPDATE_POREZ,porezi, selectedSeason);
        getResponse(request);
    }

    public Porezi searchPorezi(String season) throws Exception {
        Request request = new Request(Operation.SEARCH_POREZ,null, season);
        return (Porezi) getResponse(request).getArgument();
    }

    public void deletePorezi(Porezi porezi) throws Exception {
        Request request = new Request(Operation.DELETE_POREZ,porezi, null);
        getResponse(request);
    }

    public List<Object> getAllProfitsPerSeason() throws Exception {
        Request request = new Request(Operation.GET_ALL_PROFITS_PER_SEASON, null,null);
        return (List<Object>) getResponse(request).getArgument();
    }

    public List<InvoiceItem> getAnimalsAndNum() throws Exception {
        Request request = new Request(Operation.GET_HUNTED_ANIMALS_NUM, null,null);
        return (List<InvoiceItem>) getResponse(request).getArgument();
    }

    public List<InvoiceItem> getInvoiceItemsByReservation(Reservation reservation) throws Exception {
        Request request = new Request(Operation.GET_INVOICE_ITEMS_BY_RESERVATION, reservation,null);
        return (List<InvoiceItem>) getResponse(request).getArgument();
    }

    public List<LovackoDrustvo> getAllLovackoDrustvoByAnimals(List<Animal> animals, List<LovackoDrustvo> drustvaBySeason, List<Prices> pricesBySeason, List<Animal> animalsBySeason) {
        return new project.repository.memory.PricesRepository().getAllDrustvoByAnimals(animals, drustvaBySeason, pricesBySeason, animalsBySeason);

    }

    public List<Animal> getAllAnimalByLovackoDrustvo(LovackoDrustvo drustvo, List<Prices> pricesBySeason, List<Animal> animalsBySeason) {
        return new project.repository.memory.PricesRepository().getAllAnimalsByDrustvo(drustvo, pricesBySeason, animalsBySeason);
    }

    public List<Animal> getAllAnimalsBySeason(List<Prices> pricesesBySeason) {
        return new project.repository.memory.PricesRepository().getAllAnimalsBySeason(pricesesBySeason);
    }

    public List<LovackoDrustvo> getAllDrustvoBySeason(List<Prices> pricesBySeason) {
        return new project.repository.memory.PricesRepository().getAllDrustvaBySeason(pricesBySeason);
    }

    public List<Prices> getAllPricesByDrustvo(LovackoDrustvo drustvo, List<Prices> pricesBySeason) {
        return new project.repository.memory.PricesRepository().getAllPricesByDrustvo(drustvo, pricesBySeason);
    }

    public void setCurrentUser(User user) {
        currentUser=user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void notifyAllDrustvaPrices(List<LovackoDrustvo> drustva, Season selectedSeason) throws Exception {
        
        Request request=new Request(Operation.NOTIFY,null,selectedSeason);
        request.setList(drustva);
        getResponse(request);
    }

    public void notifyDrustvaPrices(LovackoDrustvo drustvo, Season selectedSeason) throws Exception {
        Request request= new Request(Operation.NOTIFY, drustvo,selectedSeason);
        getResponse(request);
    }

    public void sendAnimalNotAllowedNotification(Animal a) throws Exception {
        Request request= new Request(Operation.NOTIFY, a,null);
        getResponse(request);
    }

    public void sendDeleteReservation(Reservation reservation) throws Exception {
        Request request= new Request(Operation.NOTIFY, reservation,null);
        getResponse(request);
    }

  
}
