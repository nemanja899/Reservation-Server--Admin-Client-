/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Reservation;
import domain.Season;
import java.awt.HeadlessException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.UpdateReservationForm;
import project.util.tablemodel.ReservationTableModel;

/**
 *
 * @author User
 */
public class UpdateReservationFromController {

    private UpdateReservationForm frmReservationUpdate;
    private Season selectedSeason;
    private ReservationTableModel tableModel;
    private Reservation reservation;

    public UpdateReservationFromController() {
        tableModel = new ReservationTableModel();
        frmReservationUpdate = new UpdateReservationForm(MainFormControllerCordinator.getInstance().getFrmMain());
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        setCmb();
        setTable();
    }

    private void setCmb() {
        prepareSeason();
    }

    public void prepareSeason() {
        try {
            frmReservationUpdate.getCmbSeason().removeAllItems();  // Napomena sezone moraju da budu sortirane u rastucem redosledu da bi radilo!!!
            DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();

            for (Season season : getSeasonsByCurrentDate()) {
                cmbModel.addElement(season);
            }
            frmReservationUpdate.getCmbSeason().setModel(cmbModel);
            frmReservationUpdate.getCmbSeason().setSelectedIndex(-1);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmReservationUpdate, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            frmReservationUpdate.dispose();
        }
    }

    public List<Season> getSeasonsByCurrentDate() throws Exception {
        List<Season> seasonsDate = new ArrayList<>();
        List<Season> seasons = Controller.getInstance().getAllSeasons();

        if (!seasons.isEmpty()) {
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonth().getValue();

            boolean notFound = true;

            for (Season season : seasons) {
                if (notFound) {
                    if ((season.getSeasonStart() == year && month > 8) || (season.getSeasonEnd() == year && month <= 8)) {
                        notFound = false;
                        seasonsDate.add(season);
                    }
                } else {
                    seasonsDate.add(season);
                }

            }
            return seasonsDate;
        } else {

            throw new Exception("Sezone nisu ucitane");
        }
    }

    private void setTable() {
        
            frmReservationUpdate.getTblReservation().setModel(tableModel);       
    }

    public void setTableValues() throws Exception, HeadlessException {
        if (selectedSeason != null) {
            List<Reservation> reservations = Controller.getInstance().getAllReservationsBySeason(selectedSeason);
           
            if (reservations.isEmpty()) {
                JOptionPane.showMessageDialog(frmReservationUpdate, "Nema rezervacija");
            } else {
                tableModel.setReservations(reservations);
                frmReservationUpdate.getTblReservation().revalidate();
                frmReservationUpdate.getTblReservation().repaint();
            }
        } else {
            
        }
    }

    private void setListeners() {
        setBtnListeners();
        setCmbListener();

    }

    private void setCmbListener() {
        frmReservationUpdate.getCmbSeason().addActionListener((e) -> {
            try {
                if (frmReservationUpdate.getCmbSeason().getSelectedIndex() > -1) {

                    selectedSeason = (Season) frmReservationUpdate.getCmbSeason().getSelectedItem();
                     setTableValues();

                } else {
                    selectedSeason = null;
                    JOptionPane.showMessageDialog(frmReservationUpdate, "Nije izabrana sezona!!", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
               
            } catch (Exception ex) {
                Logger.getLogger(UpdateReservationFromController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frmReservationUpdate, ex.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void setBtnListeners() {
        btnDelete();
        btnBack();
    }

    public void btnDelete() {
        frmReservationUpdate.getBtnDeleteReservation().addActionListener((l) -> {
            if (frmReservationUpdate.getTblReservation().getSelectedRow() > -1) {
                reservation = tableModel.getReservations().get(frmReservationUpdate.getTblReservation().getSelectedRow());
                //  System.out.println(reservation.getDrustvo());
                int option = JOptionPane.showConfirmDialog(frmReservationUpdate, "Ponisti rezervaciju");
                if (option == 0) {
                    try {
                        Controller.getInstance().deleteReservation(reservation);
                        Controller.getInstance().sendDeleteReservation(reservation);
                        tableModel.deleteReservation(frmReservationUpdate.getTblReservation().getSelectedRow());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frmReservationUpdate, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                reservation = null;
                JOptionPane.showMessageDialog(frmReservationUpdate, "Niste selektovali rezervaciju", "ERROR", JOptionPane.ERROR_MESSAGE);

            }

        });
    }

    private void btnBack() {
        frmReservationUpdate.getBtnBack().addActionListener((l) -> {
            frmReservationUpdate.dispose();
        });
    }

    public void openForm() {
        frmReservationUpdate.setVisible(true);
    }

}
