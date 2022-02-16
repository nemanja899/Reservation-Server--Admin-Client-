/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.ViewFinanceForm;

/**
 *
 * @author User
 */
public class ViewFinanceFormController {
    
    private ViewFinanceForm frmFinance;
    
    public ViewFinanceFormController() {
        frmFinance = new ViewFinanceForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        prepareComponents();
        setListeners();
    }
    
    private void prepareComponents() {
        setTable();
    }
    
    private void setTable() {
        try {
            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Sezona", "Profit","Valuta"}, 0);
            frmFinance.getTblFinance().setModel(tableModel);
            List<Object> profits = Controller.getInstance().getAllProfitsPerSeason();
          
            if (!profits.isEmpty()) {
                for (Object obj : profits) {
                    tableModel.addRow((Object[]) obj);

                }
                
               
            } else {
                JOptionPane.showMessageDialog(frmFinance, "Nema profita");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmFinance, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void setListeners() {
        frmFinance.getBtnBack().addActionListener((l) -> {
            frmFinance.dispose();
        });
    }
    
    public void openForm(){
        frmFinance.setVisible(true);
    }
    
}
