/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.InvoiceItem;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.ViewAnimalHuntedForm;

/**
 *
 * @author User
 */
public class ViewAnimalHuntedFormController {
    
    private ViewAnimalHuntedForm frmAnimalHunted;
    
    public ViewAnimalHuntedFormController() {
        frmAnimalHunted = new ViewAnimalHuntedForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        prepareComponents();
        setListeners();
    }
    
    private void prepareComponents() {
        settable();
    }
    
    private void settable() {
        try {
            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Sezona", "Divljac", "Ulovljeno"}, 0);
            frmAnimalHunted.getTblAnimalHunted().setModel(tableModel);
            List<InvoiceItem> items = Controller.getInstance().getAnimalsAndNum();
            if (!items.isEmpty()) {
                for (InvoiceItem item : items) {
                    tableModel.addRow(new Object[]{item.getPrices().getSeason(), item.getPrices().getAnimal().getShortName(), item.getAnimalNo()});
                }
            } else {
                JOptionPane.showMessageDialog(frmAnimalHunted, "Nema ulovljenih  zivotinja");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmAnimalHunted, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setListeners() {
        frmAnimalHunted.getBtnBack().addActionListener((u) -> {
            frmAnimalHunted.dispose();
        });
    }
    public void openForm(){
        frmAnimalHunted.setVisible(true);
    }
}
