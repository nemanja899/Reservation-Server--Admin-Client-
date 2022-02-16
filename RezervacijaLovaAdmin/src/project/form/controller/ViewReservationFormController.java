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
import java.awt.BorderLayout;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import project.controller.Controller;
import project.form.components.decorator.SearchComponentDecorator;
import project.form.components.AnimalPanelComponent;
import project.form.components.ReservationTableComponent;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.ViewReservationForm;
import project.util.tablemodel.ReservationTableModel;

/**
 *
 * @author User
 */
public class ViewReservationFormController {

    private ViewReservationForm frmViewReservation;

    private ReservationTableComponent reservationTable;

    public ViewReservationFormController() {
        frmViewReservation = new ViewReservationForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);

        reservationTable = new ReservationTableComponent();
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
            List<Reservation> reservations = Controller.getInstance().getAllReservations();
            DefaultTableModel tableModel = setTableData(reservations);
            reservationTable.getTblViewReservation().setModel(tableModel);
            reservationTable.setReservations(reservations);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(reservationTable, e.getMessage());
        }

    }

    public DefaultTableModel setTableData(List<Reservation> reservations) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Lovacko Drustvo", "Lovac", "Sezona"}, 0);
        for (Reservation reservation : reservations) {
            tableModel.addRow(new Object[]{reservation.getId(), reservation.getDrustvo(), reservation.getHunter(), reservation.getSeason()});
            
        }
        return tableModel;
    }

    private void setDecorator() {
        new SearchComponentDecorator().decorate(reservationTable, ReservationTableComponent::Filter, frmViewReservation.getPnlReservation());
        frmViewReservation.getPnlReservation().revalidate();
        frmViewReservation.getPnlReservation().repaint();
    }

    private void setListeners() {
        btnBack();
        btnDetails();
    }

    public void openForm() {
        frmViewReservation.setVisible(true);
    }

    private void btnBack() {
        frmViewReservation.getBtnBack().addActionListener((g) -> {
            frmViewReservation.dispose();
        });
    }

    private void btnDetails() {
        frmViewReservation.getBtnDetails().addActionListener((v) -> {
            int row = reservationTable.getTblViewReservation().getSelectedRow();
            if (row > -1) {
                Reservation reservation = new Reservation();
                reservation.setId((Long) reservationTable.getTblViewReservation().getValueAt(row, 0));

                reservation.setDrustvo((LovackoDrustvo) reservationTable.getTblViewReservation().getValueAt(row, 1));

                reservation.setHunter((Hunter) reservationTable.getTblViewReservation().getValueAt(row, 2));

                reservation.setSeason((Season) reservationTable.getTblViewReservation().getValueAt(row, 3));

                new DetailsReservationFormController(frmViewReservation, reservation).openForm();
            } else {
                JOptionPane.showMessageDialog(reservationTable, "Niste selektovali rezervaciju");
            }
        });
    }

}
