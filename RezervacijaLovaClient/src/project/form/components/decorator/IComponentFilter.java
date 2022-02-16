/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.components.decorator;


import java.util.List;

/**
 *
 * @author User
 */
public interface IComponentFilter <T>{
    public List<T> getItems();      // item su oni podaci koji se nalaze na modelu u slucaju Jliste to su Lovacka Drustva, a u slucaju pnlAnimal to je AnimalComponent
   
    public String getDomainName();  // naslov na borderu
    
    public void clear();           // brisanje u punjenje u dekoraciji kada se upisuje u teks field
   
    public void addItem(T item);  //  dodavanje itema u model
}
