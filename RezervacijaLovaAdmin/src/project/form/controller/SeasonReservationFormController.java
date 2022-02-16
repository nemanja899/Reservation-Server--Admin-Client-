/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import domain.Season;
import project.controller.Controller;
import project.form.design.SeasonReservationForm;

/**
 *
 * @author User
 */
public class SeasonReservationFormController {

    private SeasonReservationForm frmChooseSeason;
    private Season selectedSeason;

    public SeasonReservationFormController() {
        frmChooseSeason = new SeasonReservationForm(true);
        prepareSeason();
        setListeners();

    }

    public void prepareSeason() {
        try {
            frmChooseSeason.getCmbSeason().removeAllItems();  // Napomena sezone moraju da budu sortirane u rastucem redosledu da bi radilo!!!
            DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();

            for (Season season : getSeasonsByCurrentDate()) {
                cmbModel.addElement(season);
            }
            frmChooseSeason.getCmbSeason().setModel(cmbModel);

            selectedSeason = (Season) frmChooseSeason.getCmbSeason().getSelectedItem();     // selektovana sezona pocetna ona mora prvo da se selektuje zbog ostalih komponenti
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmChooseSeason, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
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

    private void setListeners() {
        setCmbSeasonsListener();
        setBtnListener();
    }

    private void setBtnListener() {
        frmChooseSeason.getBtnReservation().addActionListener((e) -> {
            if (selectedSeason != null) {
                frmChooseSeason.dispose();
                new LovackoDrustvoReservationFormController(selectedSeason).openForm();
            } else {
                JOptionPane.showMessageDialog(frmChooseSeason, "Sezona nije selektovana", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });


    }



    // KOmbo box listener
    private void setCmbSeasonsListener() {
        frmChooseSeason.getCmbSeason().addActionListener((evt) -> {
            selectedSeason = (Season) frmChooseSeason.getCmbSeason().getSelectedItem();
        });

    }

    public void openForm() {
        frmChooseSeason.setVisible(true);

    }

}
