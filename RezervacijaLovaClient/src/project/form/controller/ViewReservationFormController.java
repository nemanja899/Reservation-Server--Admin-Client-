/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Hunter;
import domain.LovackoDrustvo;
import domain.Reservation;
import domain.Season;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;
import project.controller.Controller;
import project.form.components.decorator.SearchComponentDecorator;

import project.form.components.ReservationTableComponent;

import project.form.design.ViewReservationForm;
import project.util.FormMode;

/**
 *
 * @author User
 */
public class ViewReservationFormController {
    
    private ViewReservationForm frmViewReservation;
    
    private ReservationTableComponent reservationTable;
    private Season season;

    
    public ViewReservationFormController(Season season, JDialog dialog) {
        frmViewReservation = new ViewReservationForm(dialog, true);
        reservationTable = new ReservationTableComponent();
        this.season=season;
        prepareComponents();
        setListeners();
    }
    
    private void prepareComponents() {
        setTable();
    }
    
    private void setTable() {
        try {
            prepareTable();
            setDecorator();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmViewReservation, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            frmViewReservation.dispose();
        }
        
    }
    
    public void prepareTable() {
        try {
            List<Reservation> reservations = Controller.getInstance().getAllReservationsByDrustvoNoInvoice(season);
            DefaultTableModel tableModel = setTableData(reservations);
            reservationTable.getTblViewReservation().setModel(tableModel);
            reservationTable.setReservations(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(reservationTable, e.getMessage());
        }
        
    }
    
    public DefaultTableModel setTableData(List<Reservation> reservations) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Lovacko Drustvo", "Lovac", "Sezona"}, 0);
        for (Reservation reservation : reservations) {
            if (reservation.getSeason().equals(season)) {
                tableModel.addRow(new Object[]{reservation.getId(), reservation.getDrustvo(), reservation.getHunter(), reservation.getSeason()});
            }
            
        }
        return tableModel;
    }
    
    private void setDecorator() {
        new SearchComponentDecorator().decorate(reservationTable, ReservationTableComponent::Filter, frmViewReservation.getPnlReservation());
        frmViewReservation.getPnlReservation().revalidate();
        frmViewReservation.getPnlReservation().repaint();
    }
    
    private void setListeners() {
        
        btnAdd();
        btnBack();
        windowActivated();
    }
    
    public void openForm() {
        frmViewReservation.setVisible(true);
    }
    
    private void btnAdd() {
        frmViewReservation.getBtnAdd().addActionListener((v) -> {
            int row = reservationTable.getTblViewReservation().getSelectedRow();
            if (row > -1) {
                Reservation reservation = new Reservation();
                reservation.setId((Long) reservationTable.getTblViewReservation().getValueAt(row, 0));
                
                reservation.setDrustvo((LovackoDrustvo) reservationTable.getTblViewReservation().getValueAt(row, 1));
                
                reservation.setHunter((Hunter) reservationTable.getTblViewReservation().getValueAt(row, 2));
                
                reservation.setSeason((Season) reservationTable.getTblViewReservation().getValueAt(row, 3));
                new InvoiceFormController(reservation,null, FormMode.FORM_ADD, frmViewReservation).openForm();
            } else {
                JOptionPane.showMessageDialog(reservationTable, "Niste selektovali rezervaciju");
            }
        });
    }

    private void btnBack() {
        frmViewReservation.getBtnBack().addActionListener((l)->{
            frmViewReservation.dispose();
            
        });
    }

    private void windowActivated() {
        frmViewReservation.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                prepareTable();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
}
