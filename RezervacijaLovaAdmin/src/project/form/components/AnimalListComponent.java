/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.components;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import domain.Animal;
import project.form.components.decorator.IComponentFilter;

/**
 *
 * @author User
 */
public class AnimalListComponent extends javax.swing.JPanel implements IComponentFilter<Animal>{

    /**
     * Creates new form AnimalListComponent
     */
    private JList<Animal> listAnimals;
    private List<Animal> animals;
    public AnimalListComponent() {
        initComponents();
        listAnimals= new JList<>();
        pnlList.add(listAnimals);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlList = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnlList.setLayout(new java.awt.BorderLayout());
        add(pnlList, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlList;
    // End of variables declaration//GEN-END:variables

    @Override
    public List<Animal> getItems() {
        return animals;
    }

    @Override
    public String getDomainName() {
        return "Divljaci";
    }

    @Override
    public void clear() {
        ((DefaultListModel)listAnimals.getModel()).clear();
    }

    @Override
    public void addItem(Animal item) {
        ((DefaultListModel)listAnimals.getModel()).addElement(item);
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

   

    public List<Animal> getAnimals() {
        return animals;
    }
    
     public static boolean Filter(Animal item, String str) {
        return item.getName().toLowerCase().contains(str.toLowerCase());
    }

    public JList<Animal> getListAnimals() {
        return listAnimals;
    }
     
     
}
