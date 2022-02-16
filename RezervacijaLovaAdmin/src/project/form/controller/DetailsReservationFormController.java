/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.InvoiceItem;
import domain.Reservation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.controller.Controller;
import project.form.design.ViewDetailsReservationForm;

/**
 *
 * @author User
 */
public class DetailsReservationFormController {

    private ViewDetailsReservationForm frmDetailsreservation;
    private Reservation reservation;
    private DefaultTableModel tableModel;

    public DetailsReservationFormController(JDialog form, Reservation r) {
        frmDetailsreservation = new ViewDetailsReservationForm(form, true);
        reservation = r;
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        prepareTable();
    }

    private void prepareTable() {
        try {
            tableModel = new DefaultTableModel(new String[]{"Ulovljena divljac","Broj ulova"}, 0);
            frmDetailsreservation.getTblUlovljenaDivljac().setModel(tableModel);
            List<InvoiceItem> invoiceItems= Controller.getInstance().getInvoiceItemsByReservation(reservation);
            System.out.println(invoiceItems);
            for(InvoiceItem item:invoiceItems){
                tableModel.addRow(new Object[]{item.getPrices().getAnimal().getShortName(),item.getAnimalNo()});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmDetailsreservation, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            frmDetailsreservation.dispose();
        }
    }

    private void setListeners() {
        frmDetailsreservation.getBtnBack().addActionListener((l)->{frmDetailsreservation.dispose();});
    }

    public void openForm() {
        frmDetailsreservation.setVisible(true);
    }

}
