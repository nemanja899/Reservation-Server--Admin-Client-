/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controller;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import domain.Animal;
import domain.GeneralDObject;
import domain.Hunter;
import domain.Invoice;
import domain.InvoiceItem;
import domain.LovackoDrustvo;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.util.List;
import project.repository.connectionpool.DbConnectionPool;
import project.so.AbstractSO;
import java.sql.Connection;
import java.sql.SQLException;
import project.so.impl.AddAllPricesSO;
import project.so.AddSO;
import project.so.DeleteSO;
import project.so.GetAllByConditionSO;
import project.so.GetAllSO;
import project.so.impl.GetNumberOfHuntedAnimalsSO;
import project.so.impl.GetInvoiceItemsByReservationSO;
import project.so.impl.GetProfitsBySeasonSO;
import project.so.SearchSO;
import project.so.UpdateSO;
import project.so.impl.AddAnimalSO;
import project.so.impl.AddInvoiceSO;
import project.so.impl.AddLovackoDrustvoSO;
import project.so.impl.AddPoreziSO;
import project.so.impl.AddPricesSO;
import project.so.impl.AddReservationSO;
import project.so.impl.AddUserSO;
import project.so.impl.DeleteAnimalSO;
import project.so.impl.DeleteHunterSO;
import project.so.impl.DeleteLovackoDrustvoSO;
import project.so.impl.DeletePoreziSO;
import project.so.impl.DeletePricesSO;
import project.so.impl.DeleteReservationSO;
import project.so.impl.GetAllAnimalByConditionAllowedBySeasonByDrustvoSO;
import project.so.impl.GetAllAnimalSO;
import project.so.impl.GetAllHunterSO;
import project.so.impl.GetAllInvoiceByConditionDrustvoSO;
import project.so.impl.GetAllLovackoDrustvoByConditionWithoutPricesSO;
import project.so.impl.GetAllLovackoDrustvoSO;
import project.so.impl.GetAllPricesByConditionSeasonSO;
import project.so.impl.GetAllPricesSO;
import project.so.impl.GetAllReservationByConditionSeasonSO;
import project.so.impl.GetAllReservationByDrustvoBySeasonNoInvoiceSO;
import project.so.impl.GetAllReservationSO;
import project.so.impl.GetAllSeasonSO;
import project.so.impl.SearchAnimalSO;
import project.so.impl.SearchHunterSO;
import project.so.impl.SearchLovackoDrustvoSO;
import project.so.impl.SearchPoreziSO;
import project.so.impl.SearchPricesSO;
import project.so.impl.SearchUserSO;
import project.so.impl.UpdateAnimalSO;
import project.so.impl.UpdateHunterSO;

import project.so.impl.UpdateInvoiceSO;
import project.so.impl.UpdateLovackoDrustvoSO;
import project.so.impl.UpdatePoreziSO;
import project.so.impl.UpdatePricesSO;

/**
 *
 * @author User
 */
public class Controller {

    private static Controller instance;

    private Controller() {

    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public User login(User user) throws Exception {
        try {
            AbstractSO searchUserSO = new SearchUserSO();
            User u = (User) searchUserSO.execute(user, user.getEmail());
            if (user.getPassword().equals(u.getPassword())) {
                return u;
            }
            throw new Exception("Pogresna sifra");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }
    
     public void addUser(User user) throws Exception {
         AbstractSO aso= new AddUserSO();
         aso.execute(user, null);
    }

    public void addLovackoDrustvo(LovackoDrustvo drustvo) throws Exception {
        AbstractSO addDrustvoSO = new AddLovackoDrustvoSO();
        addDrustvoSO.execute(drustvo, null);
    }

    public LovackoDrustvo searchLovackoDrustvo(String name) throws Exception {
        AbstractSO searchDrustvoSO = new SearchLovackoDrustvoSO();
        LovackoDrustvo drustvo = new LovackoDrustvo();
        return (LovackoDrustvo) searchDrustvoSO.execute(drustvo, name);
    }

    public void updateLovackoDrustvo(LovackoDrustvo drustvo, String name) throws Exception {
        AbstractSO updateDrustvoSO = new UpdateLovackoDrustvoSO();
        updateDrustvoSO.execute(drustvo, name);
    }

    public void deleteLovackoDrustvo(LovackoDrustvo drustvo) throws Exception {
        AbstractSO deleteDrustvoSO = new DeleteLovackoDrustvoSO();
        deleteDrustvoSO.execute(drustvo, null);
    }

    public List<LovackoDrustvo> getAllLovackoDrustvo() throws Exception {
        AbstractSO getAllDrustva = new GetAllLovackoDrustvoSO();
        return (List<LovackoDrustvo>) getAllDrustva.execute(new LovackoDrustvo(), null);
    }

    public List<LovackoDrustvo> getAllDrustvoWithoutPrices(Season season) throws Exception {
        AbstractSO getAllDrustva = new GetAllLovackoDrustvoByConditionWithoutPricesSO();
        LovackoDrustvo drustvo = new LovackoDrustvo();
        return (List<LovackoDrustvo>) getAllDrustva.execute(drustvo, season);
    }

    public void addAnimal(Animal animal) throws Exception {
        AbstractSO addAnimalSO = new AddAnimalSO();
        addAnimalSO.execute(animal, null);
    }

    public void deleteAnimal(Animal animal) throws Exception {
        AbstractSO deleteAnimalSO = new DeleteAnimalSO();
        deleteAnimalSO.execute(animal, null);
    }

    public void updateAnimal(Animal animal, Long oldPk) throws Exception {
        AbstractSO upAbstractSO = new UpdateAnimalSO();
        upAbstractSO.execute(animal, oldPk);
    }

    public List<Animal> getAllAnimals() throws Exception {
        AbstractSO getAllAnimalSO = new GetAllAnimalSO();
        return (List<Animal>) getAllAnimalSO.execute(new Animal(), null);
    }

    public Animal searchAnimal(Long id) throws Exception {
        AbstractSO searchAnimal = new SearchAnimalSO();
        Animal a = new Animal();
        return (Animal) searchAnimal.execute(a, id);
    }

    public void addPrices(Prices prices) throws Exception {
        AbstractSO addPrices = new AddPricesSO();
        addPrices.execute(prices, null);
    }

    public void addAllPrices(List<Prices> prices) throws Exception {
        AbstractSO addAllPrices = new AddAllPricesSO();
        addAllPrices.execute(null, prices);
    }

    public void deletePrices(Prices prices) throws Exception {
        AbstractSO deletePrices = new DeletePricesSO();
        deletePrices.execute(prices, null);
    }

    public List<Prices> getAllPriceses() throws Exception {
        AbstractSO getAllPrices = new GetAllPricesSO();
        return (List<Prices>) getAllPrices.execute(new Prices(), null);
    }

    public List<Prices> getAllPricesBySeason(Season selectedSeason) throws Exception {
        AbstractSO getAllPrices = new GetAllPricesByConditionSeasonSO();
        Prices prices = new Prices();
        return (List<Prices>) getAllPrices.execute(prices, selectedSeason);
    }

    public Prices searchPrices(Object pk) throws Exception {
        AbstractSO getPrices = new SearchPricesSO();
        Prices prices = new Prices();
        return (Prices) getPrices.execute(prices, pk);
    }

    public void updatePrices(Prices prices, Object oldPk) throws Exception {
        AbstractSO updatePrices = new UpdatePricesSO();

        updatePrices.execute(prices, oldPk);
    }

    public void updateHunter(Hunter hunter, String passport) throws Exception {
        AbstractSO updateHunter = new UpdateHunterSO();

        updateHunter.execute(hunter, passport);
    }

    public Hunter searchHuNter(String passport) throws Exception {
        AbstractSO searchHunter = new SearchHunterSO();
        Hunter h = new Hunter();
        return (Hunter) searchHunter.execute(h, passport);

    }

    public void deleteHunter(Hunter hunter) throws Exception {
        AbstractSO deleteHunter = new DeleteHunterSO();
        deleteHunter.execute(hunter, null);
    }

    public List<Hunter> getAllHunter() throws Exception {
        AbstractSO getAllHunter = new GetAllHunterSO();
        return (List<Hunter>) getAllHunter.execute(new Hunter(), null);
    }

    public void reservate(Reservation reservation) throws Exception {
        AbstractSO addReservation = new AddReservationSO();
        addReservation.execute(reservation, null);
    }

    public List<Reservation> getAllReservations() throws Exception {
        AbstractSO getAllReservation = new GetAllReservationSO();
        return (List<Reservation>) getAllReservation.execute(new Reservation(), null);
    }

    public List<Reservation> getAllReservationsBySeason(Season season) throws Exception {
        AbstractSO getAllResBySeason = new GetAllReservationByConditionSeasonSO();
        Reservation r = new Reservation();
        return (List<Reservation>) getAllResBySeason.execute(r, season);
    }
    
      public List<Reservation> getAllReservationBySeasonByDrustvoNoInvoice(Object condition) throws Exception {
          AbstractSO aso= new GetAllReservationByDrustvoBySeasonNoInvoiceSO();
          return (List<Reservation>) aso.execute(null, condition);
    }

    public void deleteReservation(Reservation reservation) throws Exception {
        AbstractSO deleteReservation = new DeleteReservationSO();
        deleteReservation.execute(reservation, null);
    }

    public List<Season> getAllSeasons() throws Exception {
        AbstractSO getAllSeason = new GetAllSeasonSO();
        return (List<Season>) getAllSeason.execute(new Season(), null);

    }

    public void addPorezi(Porezi porezi) throws Exception {
        AbstractSO addPoreziSO = new AddPoreziSO();
        addPoreziSO.execute(porezi, null);
    }

    public void updatePorezi(Porezi porezi, Season selectedSeason) throws Exception {
        AbstractSO updatePoreziSO = new UpdatePoreziSO();
        updatePoreziSO.execute(porezi, selectedSeason.getSeason());
    }

    public Porezi searchPorezi(String season) throws Exception {
        AbstractSO searchPoreziSO = new SearchPoreziSO();
        Porezi p = new Porezi();
        return (Porezi) searchPoreziSO.execute(p, season);
    }

    public void deletePorezi(Porezi porezi) throws Exception {
        AbstractSO deletePoreziSO = new DeletePoreziSO();
        deletePoreziSO.execute(porezi, null);
    }

    public List<Object> getAllProfitsPerSeason() throws Exception {
        AbstractSO aso = new GetProfitsBySeasonSO();
        return (List<Object>) aso.execute(null, null);
    }

    public List<InvoiceItem> getInvoiceItemsByReservation(Reservation reservation) throws Exception {
        AbstractSO aso = new GetInvoiceItemsByReservationSO();
        return (List<InvoiceItem>) aso.execute(reservation, null);
    }

    public void addInvoice(Invoice invoice) throws Exception {
        AbstractSO aso= new AddInvoiceSO();
        aso.execute(invoice, null);
    }
    
     public void updateInvoice(GeneralDObject argument, Object condition) throws Exception {
         AbstractSO aso= new UpdateInvoiceSO();
         aso.execute(argument, condition);
    }

    public List<Invoice> getAllInvoiceByDrustvo(Invoice invoice, Object condition) throws Exception {
        AbstractSO aso = new GetAllInvoiceByConditionDrustvoSO();
        return (List<Invoice>) aso.execute(invoice, condition);
    }

    public List<Animal> GetAllAnimalByConditionAllowedBySeasonByDrustvo(GeneralDObject argument, Object condition) throws Exception {
        AbstractSO aso = new GetAllAnimalByConditionAllowedBySeasonByDrustvoSO();
        return (List<Animal>) aso.execute(argument, condition);
    }

    public List<InvoiceItem> getNumberOfHuntedAnimals() throws Exception {
        AbstractSO aso = new GetNumberOfHuntedAnimalsSO();
        return (List<InvoiceItem>) aso.execute(null, null);
    }

   

  

   

}
