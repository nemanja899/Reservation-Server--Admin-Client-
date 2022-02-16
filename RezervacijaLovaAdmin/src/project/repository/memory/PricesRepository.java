/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.memory;


import domain.Animal;
import domain.LovackoDrustvo;
import domain.Prices;
import domain.Season;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import project.controller.Controller;

/**
 *
 * @author User
 */
public class PricesRepository {

    List<Prices> priceses;

    public PricesRepository() {
        priceses = new ArrayList<>();
   
    }

    public List<LovackoDrustvo> getAllDrustvoByAnimals(List<Animal> animals,List<LovackoDrustvo> drustvaBySeason,List<Prices>pricesBySeason,List<Animal> animalsBySeason) {
        List<LovackoDrustvo> drustvaByAnimals = new ArrayList();
        for (LovackoDrustvo drustvo : drustvaBySeason) {
            List<Animal> animalsByDrustvo = getAllAnimalsByDrustvo(drustvo, pricesBySeason,animalsBySeason);
            if (animalsByDrustvo.containsAll(animals)) {
                drustvaByAnimals.add(drustvo);
            }
        }

        return drustvaByAnimals;
    }

    public List<Prices> getAllPricesByDrustvo(LovackoDrustvo drustvo, List<Prices> pricesBySeason) {
        List<Prices> pricesByDrustvo = new ArrayList();
        for (Prices prices : pricesBySeason) {
            if (prices.getDrustvo().equals(drustvo) && prices.getSeasonEnd().after(new Date()) && prices.getAnimal().isAllowed()) {
                pricesByDrustvo.add(prices);
            }
        }
        return pricesByDrustvo;
    }

    public List<Prices> getPricesesBySeason(Season season) {
        List<Prices> priceses=new ArrayList<>();
        for(Prices p:getPriceses()){
            if(p.getSeason().equals(season))
                priceses.add(p);
        }
        return priceses;
    }

    public List<Animal> getAllAnimalsByDrustvo(LovackoDrustvo drustvo,List<Prices>pricesBySeason,List<Animal> animalsBySeason ) {
        List<Animal> annimalsByDrustvo = new ArrayList();
        for (Animal animal :  animalsBySeason) {
            for (Prices prices : pricesBySeason) {
                if (prices.getAnimal().equals(animal) && prices.getDrustvo().equals(drustvo) && prices.getSeasonEnd().after(new Date())) {
                    annimalsByDrustvo.add(animal);
                }
            }
        }
        return annimalsByDrustvo;
    }

    public List<Prices> getAllPricesByAnimal(Animal animal, Season season) {
        List<Prices> pricesByAnimal = new ArrayList();
        for (Prices prices : priceses) {
            if (prices.getAnimal().equals(animal) && prices.getSeason().equals(season)) {
                pricesByAnimal.add(prices);
            }
        }
        return pricesByAnimal;
    }

    public List<LovackoDrustvo> getAllDrustvaBySeason(List<Prices> pricesBySeason) {
        List<LovackoDrustvo> drustvo = new ArrayList();
        for (Prices prices : pricesBySeason) {
            if (!drustvo.contains(prices.getDrustvo())) {
                
                drustvo.add(prices.getDrustvo());
            }
        }
        return drustvo;
    }

    public List<Animal> getAllAnimalsBySeason(List<Prices> pricesesBySeason) {
        List<Animal> animals = new ArrayList();
        for (Prices prices : pricesesBySeason) {
            if (!animals.contains(prices.getAnimal())) {
               
                    animals.add(prices.getAnimal());
                
            }
        }
        return animals;
    }

    public List<Prices> getPriceses() {
        return priceses;
    }
   

}
